package com.example.demo1.dto.member;

import com.example.demo1.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor
@Setter
public class SignupDTO {

        private Long id;

        @NotBlank(message = "이름은 필수 입력값입니다.")
        @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$", message = "이름은 특수문자와 숫자를 포함하지 않습니다.")
        private String name;

        @NotBlank(message = "닉네임은 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$", message = "닉네임은 특수문자를 포함하지 않습니다.")
        private String nickname;

        @NotBlank(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식이어야합니다.")
        @Pattern(regexp =  "^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$", message = "이메일은 영어 소문자와 숫자만 사용하여 4-20자리여야 합니다.")
        private String email;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
        private String password;

        private String role;

        /* DTO -> Entity */
        public Member toEntity() {
                return Member.builder()
                        .id(id)
                        .name(name)
                        .nickname(nickname)
                        .email(email)
                        .password(password)
                        .role("ROLE_MEMBER")
                        .build();
        }

//        @Builder
//        public SignupDTO(Long id, String name, String nickname, String email, String password, Role role){
//            this.id = id;
//            this.name = name;
//            this.nickname = nickname;
//            this.email = email;
//            this.password = password;
//            this.role = role;
//        }

}