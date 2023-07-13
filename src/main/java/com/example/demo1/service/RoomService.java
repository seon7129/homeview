package com.example.demo1.service;

import com.example.demo1.dto.room.RoomCheckDTO;
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

    private boolean isExistRoom(Room room){
        return roomRepository.findByOld_address(room.getOld_address(), room.getBuilding()).isPresent();
    }

    @Transactional
    public Room addRoom(Room room){
        if(isExistRoom(room)){
            throw new IllegalArgumentException("이미 존재하는 방입니다.");
        }else{
            return roomRepository.save(room);
        }
    }

    @Transactional
    public boolean deleteRoom(Long room_id){
        if(roomRepository.existsById(room_id)){
            roomRepository.deleteById(room_id);
            return true;
        }else{
            throw new IllegalArgumentException("이미 삭제된 방입니다.");
        }
    }

    public List<Room> getAllRoom(){
        return roomRepository.findAll();
    }

    public Optional<Room> check(RoomCheckDTO roomCheckDTO){
        return roomRepository.findByOld_address(roomCheckDTO.getOld_address(), roomCheckDTO.getBuilding());
    }


    public List<Room> getSido(String sido){
        return roomRepository.findBySido(sido);
    }




//    public Room roomInfo(Long room_id) throws Exception{
//        Room room = roomRepository.findById(room_id).orElseThrow(() ->
//                new IllegalArgumentException("해당 룸은 존재하지않습니다.")
//        );
//        return room;
//    }

    /*위는 확인된거*/




}