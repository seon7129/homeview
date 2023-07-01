package com.example.demo1.service;

import com.example.demo1.dto.posting.PostingContentResponseDTO;
import com.example.demo1.dto.posting.PostingSaveDTO;
import com.example.demo1.dto.posting.PostingResponseDTO;
import com.example.demo1.dto.posting.PostingUpdateDTO;
import com.example.demo1.entity.Posting;
import com.example.demo1.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;

/*    private MemberService memberService;

    private Member getMember(HttpSession session) {

        //세션 객체 안에 있는 email정보 저장
        String email = (String) session.getAttribute("email");
        //log.info("회원정보 [session GET] email:" + email);

        // email id로 찾은 member 객체 리턴
        Member info = memberService.getInfo(email);
        return info;
    }*/

    @Transactional
    public Posting save(PostingSaveDTO postingSaveDTO) {

        Posting newPosting = new Posting(
                postingSaveDTO.getPostId(),
                postingSaveDTO.getMember(),
                postingSaveDTO.getTitle(),
                postingSaveDTO.getContent(),
                postingSaveDTO.getPostTime(),
                postingSaveDTO.getPostHits(),
                postingSaveDTO.getPostLikes());

        return postingRepository.save(newPosting);
    }

    @Transactional
    public void update(Long postId, PostingUpdateDTO updateParam) {
        Posting posting = postingRepository.findById(postId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });
        posting.setTitle(updateParam.getTitle());
        posting.setContent(updateParam.getContent());
        postingRepository.save(posting);
    }

    // 글 목록
    public List<PostingResponseDTO> list() {
        List<Posting> postings = postingRepository.findAll();
        List<PostingResponseDTO> postingResponseList = new ArrayList<>();
        for (Posting posting : postings) {
            PostingResponseDTO postingResponseDTO = PostingResponseDTO.builder()
                    .postId(posting.getPostId())
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
    }

    // 댓글 목록
/*    @Transactional(readOnly = true)
    public List<Reply> replyList(Posting posting) {

        List<Reply> replies = replyRepository.findListByPosting(posting);
        if (replies == null || replies.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return replies;

    }*/


    // 글 상세보기
    @Transactional(readOnly = true)
    public PostingContentResponseDTO content(Long postId) {

        Optional<Posting> posting = postingRepository.findById(postId);

        Optional<PostingContentResponseDTO> postingResponse = Optional.ofNullable(PostingContentResponseDTO.builder()
                .postId(posting.get().getPostId())
                .memberId(posting.get().getMember().getId())
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

        Posting posting = postingRepository.findById(postId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });
        posting.setPostHits(posting.getPostHits() + 1);
        postingRepository.save(posting);
    }

/*
    public void updatePostLikes(Long postId) {
        Posting posting = postingRepository.findById(postId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });
        posting.setPostLikes(posting.getPostLikes() + 1);
        postingRepository.save(posting);
    }
*/



    @Transactional
    public void delete(Long postId) {
        postingRepository.deleteById(postId);
    }
}