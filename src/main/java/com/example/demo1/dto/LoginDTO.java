package com.example.demo1.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Builder
@NoArgsConstructor
@Setter
public class LoginDTO {

    @Email(message = "이메일 형식으로 입력하세요")
    private String email;

    private String password;

    @Builder
    public LoginDTO(String email, String password){
        this.email = email;
        this.password = password;
    }

}
