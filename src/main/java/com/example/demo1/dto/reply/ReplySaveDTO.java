package com.example.demo1.dto.reply;

import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import com.example.demo1.entity.Reply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@Setter
public class ReplySaveDTO {

    private Long postId; //FK
    /*private Long memberId; //FK*/

    @NotBlank(message = "댓글을 입력해주세요")
    @Pattern(regexp = "^.{2,50}$", message = "글자수는 2자 이상 50자 이하로 작성해주세요")
    private String content;

    private Timestamp commentTime;

    public Reply toEntity(Posting posting, Member member) {
        return Reply.builder()
                .posting(posting)
                .member(member)
                .content(content)
                .commentTime(commentTime)
                .build();
    }

}
