package com.example.demo1.service;

import com.example.demo1.dto.like.LikeSaveDTO;
import com.example.demo1.entity.Like;
import com.example.demo1.entity.Posting;
import com.example.demo1.repository.LikeRepository;
import com.example.demo1.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private PostingRepository postingRepository;

    @Transactional
    public void save(LikeSaveDTO likeSaveDTO) {

        Like newLike = new Like(
                likeSaveDTO.getLikeId(),
                likeSaveDTO.getMember(),
                likeSaveDTO.getPosting());
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
