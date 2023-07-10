package com.example.demo1.dto.member;

import com.example.demo1.entity.Member;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Builder
@NoArgsConstructor
@Setter
@AllArgsConstructor
public class UserRequestDto {
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$", message = "닉네임은 특수문자를 포함하지 않습니다.")
    private String nickname;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,16}$", message = "비밀번호는 8~16자리수여야합니다. 영문 대소문자, 숫자, 특수문자를 1개 이상 포함해야 합니다.")
    private String password;
    private String email;

}
