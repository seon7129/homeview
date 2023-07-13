package com.example.demo1.controller;

import com.example.demo1.dto.room.RoomCheckDTO;
import com.example.demo1.dto.review.ReviewDTO;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Review;
import com.example.demo1.entity.Room;
import com.example.demo1.service.MemberService;
import com.example.demo1.service.ReviewService;
import com.example.demo1.service.RoomService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/review")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final MemberService memberService;
    private final RoomService roomService;

    /*session 적용한 add*/
    // 리뷰 추가 -> 성공시 200, 실패시 400 코드
    @PostMapping("/add")
    public ResponseEntity addReview(@Valid @RequestBody ReviewDTO reviewDTO, HttpSession httpSession, BindingResult bindingResult){

        // ReviewDTO 검증 후 에러코드 반환
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage() , HttpStatus.BAD_REQUEST);
            }
        }

        // ReviewDTO를 받아올 때 member는 받아오지않고, session에 있는 회원정보로 dto에 set해준다.
        Member member = memberService.getMemberBySession(httpSession);
        reviewDTO.setMember(member);

        // 그 방의 구주소와 이름을 사용해서 해당 방이 이미 db에 있는지 체크하기위한 코드, 있다면 db에 방 추가x, 없다면 방 추가o
        Optional<Room> checkedRoom = roomService.check(new RoomCheckDTO(reviewDTO.getRoom().getOld_address(), reviewDTO.getRoom().getBuilding()));
        if(checkedRoom.isEmpty()){ // 방이 없다면
            Room addRoom = roomService.addRoom(reviewDTO.getRoom());
            reviewDTO.setRoom(addRoom);
        } else{ // 방이 있다면
            reviewDTO.setRoom(checkedRoom.get());
        }

        // 리뷰 추가 -> 성공시 200, 실패시 400 코드
        if(reviewService.addReview(reviewDTO)){
            return ResponseEntity.ok(HttpStatus.OK);
        } else{
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        }
    }


    /*session 적용한 delete*/
    // 리뷰 삭제 -> 성공시 200, 만약 잘못된 사용자가 접근시 500코드 전송
    @DeleteMapping("/delete/{review_id}")
    public ResponseEntity deleteReview(@PathVariable Long review_id, HttpSession httpSession) {

        Member member = memberService.getMemberBySession(httpSession);
        Review review = reviewService.findReview(review_id);

        if(review.getMember().equals(member)){
            reviewService.deleteReview(review_id);
            return ResponseEntity.ok(HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity all(){
        return ResponseEntity.ok(reviewService.findList());
    }

    @GetMapping("/get/{review_id}")
    public ResponseEntity get(@PathVariable Long review_id){
        return ResponseEntity.ok(reviewService.findReviewById(review_id));
    }

    @GetMapping("/search/{room_id}")
    public ResponseEntity search(@PathVariable Long room_id){
        try{
            return ResponseEntity.ok(reviewService.search(room_id));
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}