package com.example.demo1.controller;

import com.example.demo1.dto.posting.PostingResponseDTO;
import com.example.demo1.dto.review.ReviewResponseDTO;
import com.example.demo1.service.MypageService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/api/mypage")
@AllArgsConstructor
@ResponseBody
public class MypageController {

    private MypageService mypageService;

    // 좋아요 한 포스팅
    @GetMapping("/postingofLike")
    public Page<PostingResponseDTO> postingofLike(HttpSession session, @PageableDefault(sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {
        return mypageService.postingofLike(session, pageable);
    }


    // 본인이 쓴 포스팅
    @GetMapping("/postingofMember")
    public Page<PostingResponseDTO> postingofMember(HttpSession session, @PageableDefault(sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {
        return mypageService.postingofMember(session, pageable);
    }


    // 본인이 댓글 쓴 포스팅
    @GetMapping("/postingofCommentofMember")
    public Page<PostingResponseDTO> postingofCommentofMember(HttpSession session, @PageableDefault(sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {
        return mypageService.postingofCommentofMember(session, pageable);
    }

    // 본인이 쓴 리뷰 불러오기
    @GetMapping("/reviews")
    public List<ReviewResponseDTO> getReviews(HttpSession session){
        return mypageService.getReviews(session);
    }

}