package com.example.demo1.dto.posting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class LikeSaveDTO {

    private Long likeId; //시퀀스
    private Long memberId;
    private Long postId;

}
