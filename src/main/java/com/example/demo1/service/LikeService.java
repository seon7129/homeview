package com.example.demo1.service;


import com.example.demo1.dto.posting.LikeSaveDTO;
import com.example.demo1.entity.Likes;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import com.example.demo1.repository.LikeRepository;
import com.example.demo1.repository.MemberRepository;
import com.example.demo1.repository.PostingRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public boolean save(LikeSaveDTO likeSaveDTO, HttpSession session) {

        Member newMember = getInfo(session);
        Posting newPosting = makeNewPosting(likeSaveDTO.getPostId());

        boolean alreadyChecked = isAlreadyChecked(session, likeSaveDTO.getPostId());
        if (alreadyChecked == false) {
            Likes newLike = likeSaveDTO.toEntity(newMember, newPosting);
            likeRepository.save(newLike);

            Posting posting = makeNewPosting(newLike.getPosting().getPostId());
            posting.setPostLikes(posting.getPostLikes() + 1);
            postingRepository.save(posting);

            return true;
        }
        return false;
    }


    // 이미 좋아요한 포스팅일 때
    public boolean isAlreadyChecked(HttpSession session, Long postId) {

        Member newMember = getInfo(session);
        Posting newPosting = makeNewPosting(postId);

        if (likeRepository.findByMemberAndPosting(newMember, newPosting).isPresent()) {
            // 이미 좋아요 했다면
            return true;
        }
        return false;
    }

    // post에 대한 like 갯수 반환
    public int countLikes(Long postId) {
        Posting newPosting = makeNewPosting(postId);
        return newPosting.getPostLikes();
    }

    public List<Likes> list(Long postId) {
        Posting newPosting = makeNewPosting(postId);
        List<Likes> list = likeRepository.findByPosting(newPosting);
        return list;
    }

    @Transactional
    public void deleteLikesinPosting(Long postId) {
        List<Likes> list = list(postId);
        for (Likes likes : list) {
            likeRepository.deleteById(likes.getLikeId());
        }
    }


    @Transactional
    public void delete(HttpSession session, Long postId) {

        boolean alreadyChecked = isAlreadyChecked(session, postId);
        if (alreadyChecked == false)
            return;

        Member newMember = getInfo(session);
        Posting newPosting = makeNewPosting(postId);
        Likes findLike = makeNewLikes(newMember, newPosting);

        Posting posting = makeNewPosting(findLike.getPosting().getPostId());
        posting.setPostLikes(posting.getPostLikes() - 1);

        likeRepository.deleteById(findLike.getLikeId());
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

    private Likes makeNewLikes(Member member, Posting posting) {
        Likes findLike = likeRepository.findByMemberAndPosting(member, posting)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : likeId를 찾을 수 없습니다.");
                });
        return findLike;
    }

}
