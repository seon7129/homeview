package com.example.demo1.dto.review;

import com.example.demo1.entity.Member;
import com.example.demo1.entity.Review;
import com.example.demo1.entity.Room;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ReviewDTO {
    private Long review_id;

    private Member member;

    private Room room;

    @NotBlank(message = "장점을 적어주세요")
    private String pros;

    @NotBlank(message = "단점을 적어주세요")
    private String cons;

    @NotNull(message = "점수를 선택해주세요")
    private Double score;

    private Timestamp postTime;
    private String url;

    public Review toEntity(){
        return Review.builder()
                .review_id(review_id)
                .member(member)
                .room(room)
                .pros(pros)
                .cons(cons)
                .score(score)
                .postTime(postTime)
                .url(url)
                .build();
    }


}