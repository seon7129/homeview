package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;
import static jakarta.persistence.FetchType.EAGER;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_id")
    private Long review_id;

    @ManyToOne(fetch = EAGER) // 멤버 닉네임 사용안할거면 LAZY로 변경하기
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(nullable = false, length = 50)
    private String pros;

    @Column(nullable = false, length = 50)
    private String cons;

    @Column(nullable = false, length = 10)
    private Double score;

    @CreationTimestamp //시간 자동 입력
    private Timestamp postTime;

    private String url;


    @Builder
    public Review(Long review_id, Member member, Room room, String pros, String cons, Double score, Timestamp postTime, String url){
        this.review_id = review_id;
        this.member = member;
        this.room = room;
        this.pros = pros;
        this.cons = cons;
        this.score = score;
        this.postTime = postTime;
        this.url = url;
    }

}