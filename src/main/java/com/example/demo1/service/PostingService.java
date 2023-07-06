package com.example.demo1.service;

import com.example.demo1.dto.posting.PostingContentResponseDTO;
import com.example.demo1.dto.posting.PostingSaveDTO;
import com.example.demo1.dto.posting.PostingUpdateDTO;
import com.example.demo1.entity.Category;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import com.example.demo1.repository.CategoryRepository;
import com.example.demo1.repository.MemberRepository;
import com.example.demo1.repository.PostingRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;



    @Transactional
    public Posting save(PostingSaveDTO postingSaveDTO, HttpSession session) {

        Member newMember = getInfo(session);
        Category newCategory = makeNewCategory(postingSaveDTO.getCategoryId());

        Posting newPosting = postingSaveDTO.toEntity(newMember, newCategory);
        return postingRepository.save(newPosting);
    }

    @Transactional
    public void update(Long postId, PostingUpdateDTO updateParam) {

        Posting posting = makeNewPosting(postId);
        Category category = makeNewCategory(updateParam.getCategoryId());

        posting.setTitle(updateParam.getTitle());
        posting.setContent(updateParam.getContent());
        posting.setCategory(category);
        postingRepository.save(posting);
    }



    public Page<Posting> search(String keyword, Long categoryId, Pageable pageable){

        if (categoryId == 0) {
            Page<Posting> postsListAll = postingRepository.findByTitleContaining(keyword, pageable);
            return postsListAll;
        }
        else if (categoryId < 5){
            Category category = makeNewCategory(categoryId);
            Page<Posting> postsList = postingRepository.findByTitleContainingAndCategory(keyword, category, pageable);
            return postsList;
        }
        return null;

    }


    /*public List<PostingResponseDTO> allList() { // list로 반환

        List<Posting> postings = postingRepository.findAll();
        List<PostingResponseDTO> postingResponseList = new ArrayList<>();
        for (Posting posting : postings) {
            log.info(posting.getCategory().getName());
            PostingResponseDTO postingResponseDTO = PostingResponseDTO.builder()
                    .postId(posting.getPostId())
                    .categoryId(posting.getCategory().getCategoryId())
                    .memberId(posting.getMember().getId())
                    .memberNickname(posting.getMember().getNickname())
                    .title(posting.getTitle())
                    .postTime(posting.getPostTime())
                    .postHits(posting.getPostHits())
                    .postLikes(posting.getPostLikes())
                    .build();

            postingResponseList.add(postingResponseDTO);
        }
        return postingResponseList;
    }*/

    public Page<Posting> allList(Pageable pageable) { // page로 반환

        Page<Posting> postings = postingRepository.findAll(pageable);
        return postings;
    }



/*    // 글 목록
    public List<PostingResponseDTO> list(Long categoryId) { // list로 반환

        if (categoryId == 0) {
            return allList();
        }

        List<Posting> postings = postingRepository.findByCategoryId(categoryId);
        List<PostingResponseDTO> postingResponseList = new ArrayList<>();
        for (Posting posting : postings) {
            log.info(posting.getCategory().getName());
            PostingResponseDTO postingResponseDTO = PostingResponseDTO.builder()
                    .postId(posting.getPostId())
                    .categoryId(posting.getCategory().getCategoryId())
                    .memberId(posting.getMember().getId())
                    .memberNickname(posting.getMember().getNickname())
                    .title(posting.getTitle())
                    .postTime(posting.getPostTime())
                    .postHits(posting.getPostHits())
                    .postLikes(posting.getPostLikes())
                    .build();

            postingResponseList.add(postingResponseDTO);
        }
        return postingResponseList;
    }*/


    // 글 목록
    public Page<Posting> list(Long categoryId, Pageable pageable) { // page로 반환

        if (categoryId == 0) {
            return allList(pageable);
        }

        Page<Posting> postings = postingRepository.findByCategoryId(categoryId, pageable);
        return postings;
    }

    // 글 상세보기
    public PostingContentResponseDTO content(Long postId) {

        Optional<Posting> posting = postingRepository.findById(postId);

        Optional<PostingContentResponseDTO> postingResponse = Optional.ofNullable(PostingContentResponseDTO.builder()
                .postId(posting.get().getPostId())
                .memberId(posting.get().getMember().getId())
                .categoryId(posting.get().getCategory().getCategoryId())
                .memberNickname(posting.get().getMember().getNickname())
                .title(posting.get().getTitle())
                .content(posting.get().getContent())
                .postTime(posting.get().getPostTime())
                .postHits(posting.get().getPostHits() + 1) // 조회수 1 증가
                .postLikes(posting.get().getPostLikes())
                .build());

        return postingResponse
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 상세보기 실패 : postId를 찾을 수 없습니다.");
                });
    }

    public void updatePostHits(Long postId) {

        Posting posting = makeNewPosting(postId);
        posting.setPostHits(posting.getPostHits() + 1);
        postingRepository.save(posting);
    }


    @Transactional
    public void delete(Long postId) {
        postingRepository.deleteById(postId);
    }



    public boolean checkIdentification(Long postId, HttpSession session) {
        Posting posting = makeNewPosting(postId);
        Member member = getInfo(session);

        if (posting.getMember().getId() == member.getId()) {
            return true;
        }
        return false;
    }


    private Member getInfo(HttpSession session){
        String email = (String) session.getAttribute("email");
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.get();
    }

    private Posting makeNewPosting(Long postId) {
        Posting newPosting = postingRepository.findById(postId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });
        return newPosting;
    }

    private Category makeNewCategory(Long categoryId) {
        Category newCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : categoryId를 찾을 수 없습니다.");
                });
        return newCategory;
    }
}