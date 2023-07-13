package com.example.demo1.dto.review;

import com.example.demo1.entity.Room;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Getter @Setter
@NoArgsConstructor
public class ReviewResponseDTO {

    private Long review_id;
    private Long member_id;
    private String nickname;
    private Room room;
    private String pros;
    private String cons;
    private Double score;
    private Timestamp postTime;
    private String url;

    @Builder
    public ReviewResponseDTO(Long review_id, Long member_id, String nickname, Room room,
                             String pros, String cons, Double score, Timestamp postTime, String url) {
        this.review_id = review_id;
        this.member_id = member_id;
        this.nickname = nickname;
        this.room = room;
        this.pros = pros;
        this.cons = cons;
        this.score = score;
        this.postTime = postTime;
        this.url = url;
    }
}