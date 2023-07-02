package com.example.demo1.dto.posting;

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
    private Long memberId;
    private Long postId;

    public Like toEntity(Member member, Posting posting) {
        return Like.builder()
                .likeId(this.likeId)
                .member(member)
                .posting(posting)
                .build();
    }

}
