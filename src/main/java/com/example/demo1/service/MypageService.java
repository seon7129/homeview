package com.example.demo1.service;

import com.example.demo1.dto.posting.PostingResponseDTO;
import com.example.demo1.entity.Likes;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import com.example.demo1.entity.Reply;
import com.example.demo1.repository.LikeRepository;
import com.example.demo1.repository.MemberRepository;
import com.example.demo1.repository.PostingRepository;
import com.example.demo1.repository.ReplyRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {

    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final ReplyRepository replyRepository;

    // 좋아요 한 포스팅
    public Page<PostingResponseDTO> postingofLike(HttpSession session, Pageable pageable) { // page로 반환

        Member member = getInfo(session);
        List<Likes> likeList = likeRepository.findByMember(member);

        // 좋아요 한 포스팅
        List<Posting> postingList = new ArrayList<>();
        for (Likes like : likeList) {
            postingList.add(like.getPosting());
        }

        // Posting -> PostingResponseDTO
        List<PostingResponseDTO> postingResponse = postingListtoPostingResponseList(postingList);

        //list -> page
        return listtoPage(postingResponse, pageable);
    }


    // 본인이 쓴 포스팅
    public Page<PostingResponseDTO> postingofMember(HttpSession session, Pageable pageable) { // page로 반환

        Member member = getInfo(session);
        List<Posting> postingList = postingRepository.findByMember(member);

        Page<PostingResponseDTO> postingResponse = listtoPage(postingListtoPostingResponseList(postingList), pageable);
        return postingResponse;
    }


    // 본인이 댓글 쓴 포스팅
    public Page<PostingResponseDTO> postingofCommentofMember(HttpSession session, Pageable pageable) { // page로 반환

        Member member = getInfo(session);
        List<Reply> commentList = replyRepository.findByMember(member);
        List<Posting> postingList = new ArrayList<>();
        for (Reply reply : commentList) {
            postingList.add(reply.getPosting());
        }

        Page<PostingResponseDTO> postingResponse = listtoPage(postingListtoPostingResponseList(postingList), pageable);
        return postingResponse;
    }



    private Member getInfo(HttpSession session){
        String email = (String) session.getAttribute("email");
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.get();
    }

    private List<PostingResponseDTO> postingListtoPostingResponseList(List<Posting> postings){

        List<PostingResponseDTO> postingResponseList = new ArrayList<>();
        for (Posting posting : postings) {
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
    }

    private Page<PostingResponseDTO> listtoPage(List<PostingResponseDTO> postingResponse, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), postingResponse.size());
        Page<PostingResponseDTO> newPostings = new PageImpl<>(postingResponse.subList(start,end), pageable, postingResponse.size());
        return newPostings;
    }

}
