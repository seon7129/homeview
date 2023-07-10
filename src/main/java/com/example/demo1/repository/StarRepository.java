package com.example.demo1.repository;

import com.example.demo1.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StarRepository extends JpaRepository<Star, Long> {

    Optional<Star> findByMemberAndRoom(Member member, Room room);
}