package com.example.demo1.service;

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
    public Page<Posting> postingofLike(HttpSession session, Pageable pageable) { // page로 반환

        Member member = getInfo(session);
        List<Likes> likeList = likeRepository.findByMember(member);
        List<Posting> postingList = new ArrayList<>();
        for (Likes like : likeList) {
            postingList.add(like.getPosting());
        }

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), postingList.size());
        Page<Posting> newPostings = new PageImpl<>(postingList.subList(start,end), pageable, postingList.size());
        return newPostings;
    }

    // 본인이 쓴 포스팅
    public Page<Posting> postingofMember(HttpSession session, Pageable pageable) { // page로 반환

        Member member = getInfo(session);
        return postingRepository.findByMember(member, pageable);
    }

    // 본인이 댓글 쓴 포스팅
    public Page<Posting> postingofCommentofMember(HttpSession session, Pageable pageable) { // page로 반환

        Member member = getInfo(session);
        List<Reply> commentList = replyRepository.findByMember(member);
        List<Posting> postingList = new ArrayList<>();
        for (Reply reply : commentList) {
            postingList.add(reply.getPosting());
        }

        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), postingList.size());
        Page<Posting> newPostings = new PageImpl<>(postingList.subList(start,end), pageable, postingList.size());
        return newPostings;
    }



    private Member getInfo(HttpSession session){
        String email = (String) session.getAttribute("email");
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.get();
    }

}
