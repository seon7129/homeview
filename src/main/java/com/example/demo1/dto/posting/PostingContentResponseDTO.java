package com.example.demo1.dto.posting;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class PostingContentResponseDTO {
    private Long postId;
    private Long memberId;
    private Long categoryId;
    private String memberNickname;
    private String title;
    private String content;
    private Timestamp postTime;
    private int postHits;
    private int postLikes;
}