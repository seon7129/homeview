package com.example.demo1.controller;

import com.example.demo1.dto.RoomDTO;
import com.example.demo1.dto.member.MemberResponseDTO;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Room;
import com.example.demo1.service.MemberService;
import com.example.demo1.service.PostingService;
import com.example.demo1.service.RoomService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private RoomService roomService;

    public Member checkAdmin(HttpSession session){
        log.info("세션값 = " + session);
        String email = (String) session.getAttribute("email");
        log.info("회원정보 [session GET] email:" + email);

        // email id로 찾은 member 리턴
        Member info = memberService.getInfo(email);
        log.info("ROLE값: " + info.getRole());
        return info;
    }


    @GetMapping("/healthcheck")
    public ResponseEntity<Integer> health_check(HttpSession session){

        Member info = checkAdmin(session);
        if(info.getRole().equals("ADMIN")){
            return ResponseEntity.ok().body(200);
        }else{
            return ResponseEntity.ok().body(201); // 변경필요
        }
    }

    @GetMapping("/list/member")
    public List<MemberResponseDTO> getAllMembers(HttpSession session){
        Member info = checkAdmin(session);
        if(info.getRole().equals("ADMIN")){
            return memberService.getAllMember();// 바디형식으로 전송해줘야할수도
        }else{
            return null;// 변경필요
        }
    }

    @GetMapping("/list/room")
    public List<Room> getAllRooms(HttpSession session){
        Member info = checkAdmin(session);
        if(info.getRole().equals("ADMIN")){
            return roomService.getAllRooms();
        }else{
            return null;
        }
    }



    @DeleteMapping("/posting/{post_id}")
    public void deletePosting(@PathVariable Long post_id, HttpSession session){
        Member info = checkAdmin(session);
        if(info.getRole().equals("ADMIN")){
            postingService.delete(post_id);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id, HttpSession session){
        Member info = checkAdmin(session);
        if(info.getRole().equals("ADMIN")){
            memberService.deleteById(id);
        }
    }


    // 글 삭제
    // 리뷰 삭제

}
