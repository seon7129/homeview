package com.example.demo1.repository;

import com.example.demo1.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {


    //@Query("select r from Room r where r.new_address like %:new_address%")
    @Query("select r from Room r where r.old_address =:old_address and r.building =:building")
    Optional<Room> findByOld_address(String old_address, String building);
    @Override
    Optional<Room> findById(Long room_id);

    boolean existsById(Long room_id);

    @Query("select r from Room r where r.sido =:sido")
    List<Room> findBySido(String sido);
}