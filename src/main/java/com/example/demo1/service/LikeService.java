package com.example.demo1.service;


import com.example.demo1.dto.posting.LikeSaveDTO;
import com.example.demo1.entity.Likes;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import com.example.demo1.repository.LikeRepository;
import com.example.demo1.repository.MemberRepository;
import com.example.demo1.repository.PostingRepository;
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
    public boolean save(LikeSaveDTO likeSaveDTO) {

        Member newMember = makeNewMember(likeSaveDTO.getMemberId());
        Posting newPosting = makeNewPosting(likeSaveDTO.getPostId());

        boolean alreadyChecked = isAlreadyChecked(likeSaveDTO.getMemberId(), likeSaveDTO.getPostId());
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
    public boolean isAlreadyChecked(Long memberId, Long postId) {

        Member newMember = makeNewMember(memberId);
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
    public void delete(Long memberId, Long postId) {

        boolean alreadyChecked = isAlreadyChecked(memberId, postId);
        if (alreadyChecked == false)
            return;

        Member newMember = makeNewMember(memberId);
        Posting newPosting = makeNewPosting(postId);
        Likes findLike = makeNewLikes(newMember, newPosting);

        Posting posting = makeNewPosting(findLike.getPosting().getPostId());
        posting.setPostLikes(posting.getPostLikes() - 1);

        likeRepository.deleteById(findLike.getLikeId());
    }


    private Member makeNewMember(Long memberId) {
        Member newMember = memberRepository.findById(memberId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : memberId를 찾을 수 없습니다.");
                });
        return newMember;
    }

    public Posting makeNewPosting(Long postId) {
        Posting newPosting = postingRepository.findById(postId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });
        return newPosting;
    }

    public Likes makeNewLikes(Member member, Posting posting) {
        Likes findLike = likeRepository.findByMemberAndPosting(member, posting)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : likeId를 찾을 수 없습니다.");
                });
        return findLike;
    }

}
