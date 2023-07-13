package com.example.demo1.controller;

import com.example.demo1.dto.room.RoomCheckDTO;
import com.example.demo1.entity.Room;
import com.example.demo1.service.RoomService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/room")
@AllArgsConstructor
@ResponseBody
public class RoomController {

    private final RoomService roomService;

//    @GetMapping("/check/{new_address}") // 방이 존재하지않으면 201코드, 존재하면 room 반환
//    public ResponseEntity check(@PathVariable String new_address){
//        try{
//            return ResponseEntity.ok(roomService.check(new_address));
//        } catch (Exception e){
//            return new ResponseEntity<>(HttpStatus.CREATED);
//        }
//    }

    @GetMapping("/check")
    public ResponseEntity check(@RequestBody RoomCheckDTO roomCheckDTO){
        try{
            return ResponseEntity.ok(roomService.check(roomCheckDTO));
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }

    // 추가성공시 true, 이미 존재하면 false
    @PostMapping("/add")
    public ResponseEntity addRoom(Room room){
        try{
            return ResponseEntity.ok(roomService.addRoom(room));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{room_id}")
    public ResponseEntity deleteRoom(@PathVariable Long room_id){
        try{
            return ResponseEntity.ok(roomService.deleteRoom(room_id));
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sido/{sido}")
    public List<Room> getSido(@PathVariable String sido){
        return roomService.getSido(sido);
    }

}