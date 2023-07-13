package com.example.demo1.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewAdminDTO {

    private Long review_id;
    private Long member_id;
    private String nickname;
    private String building;

    @Builder
    public ReviewAdminDTO(Long review_id, Long member_id, String nickname, String building) {
        this.review_id = review_id;
        this.member_id = member_id;
        this.nickname = nickname;
        this.building = building;
    }
}