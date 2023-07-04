package com.example.demo1.dto.posting;

import com.example.demo1.entity.Likes;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class LikeSaveDTO {

    /*private Long memberId;*/
    private Long postId;

    public Likes toEntity(Member member, Posting posting) {
        return Likes.builder()
                .member(member)
                .posting(posting)
                .build();
    }

}
