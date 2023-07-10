package com.example.demo1.dto.member;

import com.example.demo1.entity.Member;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberResponseDTO {

    private Long id;
    private String name;
    private String nickname;
    private String email;
    private String role;

    public Member toEntity(){
        Member member = Member.builder()
                .id(id)
                .name(name)
                .nickname(nickname)
                .email(email)
                .role(role)
                .build();
        return member;
    }

    @Builder
    public MemberResponseDTO(Long id, String name, String nickname, String email, String role){
            this.id = id;
            this.name = name;
            this.nickname = nickname;
            this.email = email;
            this.role = role;
        }
}
