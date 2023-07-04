package com.example.demo1.repository;

import com.example.demo1.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("select r from Reply r left join r.posting where r.posting.postId =:postId order by  r.commentId desc")
    List<Reply> findByPostId(Long postId);

}
