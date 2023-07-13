package com.example.demo1.service;

import com.example.demo1.dto.member.LoginDTO;
import com.example.demo1.dto.member.MemberResponseDTO;
import com.example.demo1.dto.member.SignupDTO;
import com.example.demo1.dto.member.UserRequestDto;
import com.example.demo1.entity.Member;
import com.example.demo1.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public Member joinUser(SignupDTO signupDTO){
        signupDTO.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        return memberRepository.save(signupDTO.toEntity());
    }



    public Member getMemberBySession(HttpSession session){
        String email = (String) session.getAttribute("email");
        return memberRepository.findByEmail(email).get();
    }

    public Boolean checkEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email);
    }

    public String loginUser(LoginDTO loginDTO){
        Member findMember = memberRepository.findByEmail(loginDTO.getEmail()).get();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(findMember == null){
            return "false";
        }

        if(!passwordEncoder.matches(loginDTO.getPassword(), findMember.getPassword())){
            return "false";
        }

        return findMember.getEmail();
    }

    public Member getInfo(String email){
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.get();
    }

    @Transactional
    public void update(UserRequestDto dto){

        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("해당 회원이 존재하지않습니다.")
        );

        // 암호화되지않은 비번이 들어온다
        if(passwordEncoder.matches(dto.getPassword(), member.getPassword())){
            member.update(member.getPassword(), dto.getNickname());
        }
        else{
            String encodePW = passwordEncoder.encode(dto.getPassword());
            member.update(encodePW, dto.getNickname());
        }
    }


    public boolean comparePW(Member member, String checkPW){
        if(!passwordEncoder.matches(checkPW, member.getPassword())){
            return false;
        }
        return true;
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }


    public List<MemberResponseDTO> getAllMember(){
        //return memberRepository.findAll();
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDTO> memberDtoList = new ArrayList<>();
        for (Member member : members) {
            MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .nickname(member.getNickname())
                    .email(member.getEmail())
                    .role(member.getRole())
                    .build();

            memberDtoList.add(memberResponseDTO);
        }
        return memberDtoList;
    }
}