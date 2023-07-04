package com.example.demo1.dto.posting;


import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class PostingResponseDTO {

    private Long postId;
    private Long categoryId;
    private Long memberId;
    private String memberNickname;
    private String title;
    private Timestamp postTime;
    private int postHits;
    private int postLikes;


}
