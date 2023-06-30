package com.example.demo1.dto.posting;

import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@Setter
public class PostingSaveDTO {

    private Long postId; //시퀀스
    private Member member;

    @NotBlank(message = "제목을 입력해주세요")
    @Pattern(regexp = "^.{2,50}$", message = "2 ~ 50 자리의 제목을 작성해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    @Pattern(regexp = "^.{2,500}$", message = "글자수는 2자 이상 500자 이하로 작성해주세요")
    private String content;

    private Timestamp postTime;
    private int postHits;
    private int postLikes;
    //private List<Reply> comment;

    public Posting toEntity() {
        return Posting.builder()
                .postId(postId)
                .member(member)
                .title(title)
                .content(content)
                .postTime(postTime)
                .postHits(0)
                .postLikes(0)
                .build();
    }
}