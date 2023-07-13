package com.example.demo1.service;

import com.example.demo1.dto.posting.PostingResponseDTO;
import com.example.demo1.dto.review.ReviewResponseDTO;
import com.example.demo1.entity.*;
import com.example.demo1.repository.*;
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
    private final ReviewRepository reviewRepository;

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

    public List<ReviewResponseDTO> getReviews(HttpSession session){
        Member member = getInfo(session);
        List<Review> reviews = reviewRepository.findByMember_id(member.getId());
        List<ReviewResponseDTO> reviewDtoList = new ArrayList<>();
        for (Review review : reviews) {
            ReviewResponseDTO reviewResponseDTO = ReviewResponseDTO.builder()
                    .review_id(review.getReview_id())
                    .member_id(review.getMember().getId())
                    .nickname(review.getMember().getNickname())
                    .room(review.getRoom())
                    .pros(review.getPros())
                    .cons(review.getCons())
                    .score(review.getScore())
                    .postTime(review.getPostTime())
                    .url(review.getUrl())
                    .build();

            reviewDtoList.add(reviewResponseDTO);
        };
        return reviewDtoList;
    }

}