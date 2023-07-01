package com.example.demo1.dto.like;

import com.example.demo1.entity.Like;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class LikeSaveDTO {

    private Long likeId; //시퀀스
    private Member member;
    private Posting posting;

    public Like toEntity() {
        return Like.builder()
                .likeId(likeId)
                .member(member)
                .posting(posting)
                .build();
    }

}
