package com.example.demo1.service;

import com.example.demo1.dto.RoomDTO;
import com.example.demo1.dto.posting.PostingSaveDTO;
import com.example.demo1.entity.Posting;
import com.example.demo1.entity.Room;
import com.example.demo1.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    @Transactional
    public void addRoom(RoomDTO roomDTO){
        roomRepository.save(roomDTO.toEntity());
    }

    public void deleteRoom(Long room_id){
        roomRepository.deleteById(room_id);
    }

    public Room roomInfo(Long room_id) throws Exception{
        Room room = roomRepository.findById(room_id).orElseThrow(() ->
                new IllegalArgumentException("해당 룸은 존재하지않습니다.")
        );

        return room;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
}