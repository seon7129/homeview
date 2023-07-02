package com.example.demo1.repository;

import com.example.demo1.entity.Likes;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByMemberAndPosting(Member member, Posting posting);
}
