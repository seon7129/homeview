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

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostingRepository postingRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(LikeSaveDTO likeSaveDTO) {

        Member newMember = memberRepository.findById(likeSaveDTO.getMemberId())
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : memberId를 찾을 수 없습니다.");
                });

        Posting newPosting = postingRepository.findById(likeSaveDTO.getPostId())
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });

        // 이미 좋아요되어있으면 에러 반환
        if (likeRepository.findByMemberAndPosting(newMember, newPosting).isPresent()) {
            throw new IllegalArgumentException("이미 좋아요한 포스팅 입니다.");
        }

        Likes newLike = likeSaveDTO.toEntity(newMember, newPosting);
        likeRepository.save(newLike);


        Posting posting = postingRepository.findById(newLike.getPosting().getPostId())
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });
        posting.setPostLikes(posting.getPostLikes() + 1);
        postingRepository.save(posting);

    }

    @Transactional
    public void delete(Long likeId) {

        Likes findLike = likeRepository.findById(likeId)
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : likeId를 찾을 수 없습니다.");
                });


        Posting posting = postingRepository.findById(findLike.getPosting().getPostId())
                .orElseThrow(() -> { // 영속화
                    return new IllegalArgumentException("글 찾기 실패 : postId를 찾을 수 없습니다.");
                });
        posting.setPostLikes(posting.getPostLikes() - 1);

        likeRepository.deleteById(likeId);
    }

}
