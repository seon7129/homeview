package com.example.demo1.repository;

import com.example.demo1.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

//    @Query("select m.id,m.name,m.nickname,m.email,m.role from Member m")
    List<Member> findAll();
}
