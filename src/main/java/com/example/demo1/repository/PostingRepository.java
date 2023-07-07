package com.example.demo1.repository;

import com.example.demo1.entity.Category;
import com.example.demo1.entity.Member;
import com.example.demo1.entity.Posting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {

    @Query("select p from Posting p left join p.category where p.category.categoryId =:categoryId  order by  p.postId desc")
    List<Posting> findByCategoryId(Long categoryId);

    @Query("select p from Posting p order by  p.postId desc")
    List<Posting> findAll();

    //@Query("select p from Posting p order by  p.postId desc")
    List<Posting> findByMember(Member member);

    //@Query("select p from Posting p left join p.category where p.category.categoryId =:categoryId  order by  p.postId desc")
    List<Posting> findByTitleContainingAndCategory(String title, Category category);

    List<Posting> findByTitleContaining(String title);

}