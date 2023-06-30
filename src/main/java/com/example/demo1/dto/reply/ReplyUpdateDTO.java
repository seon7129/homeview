package com.example.demo1.dto.reply;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReplyUpdateDTO {

    @NotBlank(message = "댓글을 입력해주세요")
    @Pattern(regexp = "^.{2,50}$", message = "글자수는 2자 이상 50자 이하로 작성해주세요")
    private String content;

    public ReplyUpdateDTO() {
    }

    public ReplyUpdateDTO(String content) {
        this.content = content;
    }
}