package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Likes")
public class Likes { // like는 데이터베이스의 예약어..

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="like_id")
    private Long likeId;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER) //Many = Posting, One = Member, 한 계정에 여러 개 글 작성
    @JoinColumn(name = "member_id")
    private Member member; // FK

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posting posting; //FK

    @Builder
    public Likes(Member member, Posting posting) {
        this.member = member;
        this.posting = posting;
    }
}
