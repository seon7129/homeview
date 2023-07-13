package com.example.demo1.repository;

import com.example.demo1.entity.Member;
import com.example.demo1.entity.Review;
import com.example.demo1.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByMemberAndRoom(Member member, Room room);
    //@Query("select r from Review r left join r.room where r.room.room_id =:room_id order by r.review_id desc")
    // --> 페이지에서 review_id를 desc로 정렬해줘서 쿼리문에서는 설정하지않았다.
    @Query("select r from Review r left join r.room where r.room.room_id =:room_id order by r.review_id desc")
    List<Review> findByRoomId(Long room_id);

    @Query("select r from Review r where r.member.id=:id")
    List<Review> findByMember_id(Long id);

    @Query("select r from Review r order by r.review_id desc")
    List<Review> findAll();
}