package com.example.demo1.service;


import com.example.demo1.dto.posting.LikeSaveDTO;
import com.example.demo1.entity.Like;
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
    private PostingRepository postingRepository;
    private MemberRepository memberRepository;

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

        Like newLike = likeSaveDTO.toEntity(newMember, newPosting);'
    // 이 부분 안됨... ddl 에러남
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

        Like findLike = likeRepository.findById(likeId)
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
