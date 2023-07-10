package com.example.demo1.controller;

import com.example.demo1.dto.RoomDTO;
import com.example.demo1.entity.Room;
import com.example.demo1.service.RoomService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/room")
@AllArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // 반환해줬으면하는거 있는지 물어보기
    @PostMapping("/add")
    public void addRoom(@RequestBody RoomDTO roomDTO){
        roomService.addRoom(roomDTO);
    }

    @DeleteMapping("/delete/{room_id}")
    public void deleteRoom(@PathVariable Long room_id){
        roomService.deleteRoom(room_id);
    }

    // 방 정보 조회
    @GetMapping("/info/{room_id}")
    public Room infoRoom(@PathVariable Long room_id){
        try {
            return roomService.roomInfo(room_id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}