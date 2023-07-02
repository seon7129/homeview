package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "posting")
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long postId; //시퀀스

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.EAGER) //Many = Posting, One = Member, 한 계정에 여러 개 글 작성
    @JoinColumn(name = "member_id")
    private Member member; // FK

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category; // FK

    @Column(nullable = false, length = 50)
    private String title;

    //@Lob //대용량 데이터 4GB
    @Column(nullable = false, length = 500)
    private String content; //섬머노트 라이브러리

    @CreationTimestamp //시간 자동 입력
    private Timestamp postTime;

    private int postHits; //조회수

    private int postLikes; // 좋아요수


    @Builder
    public Posting(Member member, Category category, String title, String content, Timestamp postTime, int postHits, int postLikes) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.postTime = postTime;
        this.postHits = postHits;
        this.postLikes = postLikes;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPostHits(int postHits) {
        this.postHits = postHits;
    }

    public void setPostLikes(int postLikes) {
        this.postLikes = postLikes;
    }
}