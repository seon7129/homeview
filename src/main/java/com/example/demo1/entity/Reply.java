package com.example.demo1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "comment")
public class Reply { // Comment는 키워드여서 Reply로 했음.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long commentId;

    //@JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posting posting; //FK

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; //FK

    @Column(nullable = false, length = 50)
    private String content;

    @CreationTimestamp
    private Timestamp commentTime;


    @Builder
    public Reply(Posting posting, Member member, String content, Timestamp commentTime) {
        this.posting = posting;
        this.member = member;
        this.content = content;
        this.commentTime = commentTime;
    }

    public void setContent(String comment) {
        this.content = comment;
    }
}