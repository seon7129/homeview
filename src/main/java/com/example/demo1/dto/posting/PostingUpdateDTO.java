package com.example.demo1.dto.posting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostingUpdateDTO {

    @NotBlank(message = "제목을 입력해주세요")
    @Pattern(regexp = "^.{2,50}$", message = "2 ~ 50 자리의 제목을 작성해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @Pattern(regexp = "^.{2,500}$", message = "글자수는 2자 이상 500자 이하로 작성해주세요")
    private String content;

    public PostingUpdateDTO() {
    }

    public PostingUpdateDTO(String title, String content, String memberName) {
        this.title = title;
        this.content = content;
    }
}
