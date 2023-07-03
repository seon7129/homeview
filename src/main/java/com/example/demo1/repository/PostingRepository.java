package com.example.demo1.repository;

import com.example.demo1.entity.Posting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {

    @Query("select p from Posting p order by  p.postId desc")
    List<Posting> findAll();

    Page<Posting> findByTitleContaining(String keyword, Pageable pageable);
}