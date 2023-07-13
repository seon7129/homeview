package com.example.demo1.controller;

import com.example.demo1.dto.member.CheckPwDTO;
import com.example.demo1.dto.member.LoginDTO;
import com.example.demo1.dto.member.SignupDTO;
import com.example.demo1.dto.member.UserRequestDto;
import com.example.demo1.entity.Member;
import com.example.demo1.service.MemberService;
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


// 중복가입 추가 o
// 에러 처리
@Slf4j
@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class MemberController {
    private MemberService memberService;

    @GetMapping("/healthcheck")
    public ResponseEntity<Integer> health_check(){
        return ResponseEntity.ok().body(200);
    }

    @PostMapping("/join")
    public ResponseEntity execSignup(@Valid @RequestBody SignupDTO signupDTO, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        memberService.joinUser(signupDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    // [중복가입] True -> 중복, False -> 중복x
    @GetMapping("/join/{email}/exists")
    public ResponseEntity<Boolean> checkEmailDuplicate(@PathVariable String email){
        return ResponseEntity.ok(memberService.checkEmailDuplicate(email));
    }


    @PostMapping("/login")
    public ResponseEntity execLogin(@RequestBody LoginDTO loginDTO, HttpSession session){

        String result = memberService.loginUser(loginDTO);
        //login 실패
        if(result == "false"){
            return ResponseEntity.ok(HttpStatus.BAD_REQUEST);
        } else {
            //login 성공
            session.setAttribute("email", result);
            session.setMaxInactiveInterval(1800); // 60s * 30 (30분)

            // member 객체
            Member info = memberService.getInfo(result);
            return ResponseEntity.ok(info);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity execLogout(HttpSession session){
        if(session!=null){
            session.invalidate(); // 진짜 지워진지 마이페이지 확인하면서 확인하기
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 세션의 email key로 member객체 반환
    @GetMapping("/info")
    public ResponseEntity infoGet(HttpSession session) throws Exception{
        Member info = memberService.getMemberBySession(session);
        return ResponseEntity.ok(info);
    }

    @PostMapping("/checkPW")
    public ResponseEntity checkPW(@RequestBody CheckPwDTO checkPwDTO, HttpSession session){
        Member info = memberService.getMemberBySession(session);
        boolean result = memberService.comparePW(info, checkPwDTO.getPassword());
        if(result == false)
            return ResponseEntity.ok(HttpStatus.EXPECTATION_FAILED); //417
        else {
            return ResponseEntity.ok(HttpStatus.OK);
        }
    }

    // 여기서 파리미터에 있는 member객체는 변경될 값이다
    @PostMapping("/update")
    public ResponseEntity update(@Valid @RequestBody UserRequestDto userRequestDto, HttpSession session, BindingResult bindingResult) throws Exception{

        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for(FieldError error : list) {
                return new ResponseEntity<>(error.getDefaultMessage(), HttpStatus.BAD_REQUEST);
            }
        }

        memberService.update(userRequestDto);
        Member info = memberService.getInfo(userRequestDto.getEmail());
        return ResponseEntity.ok(info);
    }



    // 마이페이지 구현
    // 1. 세션 인증
    // 2. 유저정보 보내주기(조회)
    // 3. 비밀번호 변경
    // 4. 닉네임 변경


}