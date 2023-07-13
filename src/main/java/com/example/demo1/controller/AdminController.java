package com.example.demo1.controller;

import com.example.demo1.dto.member.MemberResponseDTO;
import com.example.demo1.dto.posting.PostingResponseDTO;
import com.example.demo1.dto.reply.ReplyResponseDTO;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Review;
import com.example.demo1.service.*;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin")
@AllArgsConstructor
@ResponseBody
public class AdminController {
    private MemberService memberService;
    private PostingService postingService;
    private LikeService likeService;
    private ReplyService replyService;
    private ReviewService reviewService;
    private RoomService roomService;

    public Member checkAdmin(HttpSession session){
        String email = (String) session.getAttribute("email");
        // email id로 찾은 member 리턴
        Member member = memberService.getInfo(email);
        return member;
    }

    // 어드민 체크 정삭동작하는지 확인
    @GetMapping("/healthcheck")
    public ResponseEntity<Integer> health_check(HttpSession session){

        Member member = checkAdmin(session);
        if(member.getRole().equals("ADMIN")){
            return ResponseEntity.ok().body(200);
        }else{
            return ResponseEntity.ok().body(201); // 변경필요
        }
    }

    // 회원정보 불러오는거 Page로 변경하는게 나을지도 ?
    @GetMapping("/list")
    public List<MemberResponseDTO> getAllMembers(HttpSession session){
        Member member = checkAdmin(session);
        if(member.getRole().equals("ADMIN")){
            return memberService.getAllMember();// 바디형식으로 전송해줘야할수도
        }else{
            return null;// 변경필요
        }
    }

    // 유저 삭제
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, HttpSession session){
        Member member = checkAdmin(session);
        if(member.getRole().equals("ADMIN")){
            memberService.deleteById(id);
        }
    }

    // 글 리스트로 불러오기
    @GetMapping("/posting/list")
    public List<PostingResponseDTO> getAllPostings(HttpSession session) {
        Member member = checkAdmin(session);
        if(member.getRole().equals("ADMIN")){
            List<PostingResponseDTO> postingResponseDTO = postingService.allPostingsinAdmin();
            return postingResponseDTO;
        }
        return null;
    }


    // 글 삭제 --> 코멘트, 좋아요도 같이 삭제
    @DeleteMapping("/posting/{post_id}")
    public void deletePosting(@PathVariable Long post_id, HttpSession session){
        Member member = checkAdmin(session);
        if(member.getRole().equals("ADMIN")){
            likeService.deleteLikesinPosting(post_id);
            replyService.deleteRepliesinPosting(post_id);
            postingService.delete(post_id);
        }
    }

    // 댓글 리스트로 불러오기
    @GetMapping("/comment/list")
    public List<ReplyResponseDTO> getAllReplies(HttpSession session) {
        Member member = checkAdmin(session);
        if(member.getRole().equals("ADMIN")){
            List<ReplyResponseDTO> replyResponseDTO = replyService.allRepliesinAdmin();
            return replyResponseDTO;
        }
        return null;
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{comment_id}")
    public void deleteReply(@PathVariable Long comment_id, HttpSession session) {
        Member member = checkAdmin(session);
        if(member.getRole().equals("ADMIN")){
            replyService.delete(comment_id);
        }
    }


    // 리뷰 삭제 --> 사진도 같이 삭제
    @DeleteMapping("/review/{review_id}")
    public void deleteReview(@PathVariable Long review_id, HttpSession session){
        Member member = checkAdmin(session);
        if(member.getRole().equals("ADMIN")){
            reviewService.deleteReview(review_id);
        }
    }

    // 회원 id로 리뷰(review_id, member_id, nickname, room -> building) 불러오기
    @GetMapping("/review/{id}")
    public ResponseEntity getReviewsById(@PathVariable Long id, HttpSession session){
        Member member = checkAdmin(session);
        if(member.getRole().equals("ADMIN")){
            return ResponseEntity.ok(reviewService.findReviewByMemberId(id));
        } else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 방 삭제 -> 관련된 리뷰, 사진 삭제/ 성공시 200, 실패시 400
    @DeleteMapping("/room/{room_id}")
    public void deleteRoom(@PathVariable Long room_id, HttpSession session){
        Member member = checkAdmin(session);
        if(member.getRole().equals("ADMIN")){
            List<Review> reviews = reviewService.findReviewByRoomId(room_id);
            for (Review review : reviews) {
                reviewService.deleteReview(review.getReview_id());
            }
            roomService.deleteRoom(room_id);
        }
    }

    // 방 정보 불러오기
    @GetMapping("/room/list")
    public ResponseEntity getAllRooms(HttpSession session){
        Member member = checkAdmin(session);
        if(member.getRole().equals("ADMIN")){
            return ResponseEntity.ok(roomService.getAllRoom());
        } else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}