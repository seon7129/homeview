package com.example.demo1.dto.reply;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class ReplyResponseDTO {

    private Long commentId;
    private Long postId;
    private Long memberId;
    private String memberNickName;
    private String content;
    private Timestamp commentTime;

}
