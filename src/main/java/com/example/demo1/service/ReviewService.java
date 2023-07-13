package com.example.demo1.service;

import com.example.demo1.dto.review.ReviewAdminDTO;
import com.example.demo1.dto.review.ReviewDTO;
import com.example.demo1.dto.review.ReviewResponseDTO;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Review;
import com.example.demo1.entity.Room;
import com.example.demo1.repository.MemberRepository;
import com.example.demo1.repository.ReviewRepository;
import com.example.demo1.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final S3UploadService s3UploadService;
    private final RoomService roomService;

    public ReviewResponseDTO findReviewById(Long review_id){
        Review review = reviewRepository.findById(review_id).get();
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
        return reviewResponseDTO;
    }

    public Review findReview(Long review_id){
        return reviewRepository.findById(review_id).get();
    }

    public List<ReviewResponseDTO> findList(){
        List<ReviewResponseDTO> reviewDtoList = new ArrayList<>();
        List<Review> reviews = reviewRepository.findAll();
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
        }
        return reviewDtoList;
    }

    // 해당 멤버의 해당 룸에 대한 정보가 존재하면 false, 존재하지않으면 true 반환
    @Transactional
    public boolean addReview(ReviewDTO reviewDTO) {

        Member member = memberRepository.findById(reviewDTO.getMember().getId()).get();
        Room room = roomRepository.findById(reviewDTO.getRoom().getRoom_id()).get();

        if(reviewRepository.findByMemberAndRoom(member, room).isEmpty()){
            reviewRepository.save(reviewDTO.toEntity());
            return true;
        }
        return false;

    }

    @Transactional
    public void deleteReview(Long review_id){
        Review review = reviewRepository.findById(review_id).get();

        // 이미지가 존재하면 이미지도 삭제
        if(review.getUrl().isEmpty()){
            reviewRepository.deleteById(review_id);
        } else {
            s3UploadService.deleteImage(review.getUrl());
            reviewRepository.deleteById(review_id);
        }

        Long room_id = review.getRoom().getRoom_id();

        // 해당 방에 리뷰가 하나도 없다면 방 삭제
        if(findReviewByRoomId(room_id).isEmpty()){
            try {
                roomService.deleteRoom(room_id);
            } catch (Exception e){
                throw new IllegalArgumentException("잘못된 요청입니다.");
            }
        }
    }

    public List<ReviewResponseDTO> search(Long room_id){
        List<Review> reviews = reviewRepository.findByRoomId(room_id);
        if(reviews.isEmpty()){
            throw new IllegalArgumentException("존재하지않는 방입니다.");
        }

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
        }
        return reviewDtoList;
    }

    // 어드민에서 사용할거
    public List<ReviewAdminDTO> findReviewByMemberId(Long id){
        List<Review> reviews = reviewRepository.findByMember_id(id);
        List<ReviewAdminDTO> reviewDtoList = new ArrayList<>();
        for (Review review : reviews) {
            ReviewAdminDTO reviewAdminDTO = ReviewAdminDTO.builder()
                    .review_id(review.getReview_id())
                    .member_id(review.getMember().getId())
                    .nickname(review.getMember().getNickname())
                    .building(review.getRoom().getBuilding())
                    .build();

            reviewDtoList.add(reviewAdminDTO);
        }
        return reviewDtoList;
    }

    // 어드민에서 사용
    public List<Review> findReviewByRoomId(Long room_id){
        return reviewRepository.findByRoomId(room_id);
    }



}