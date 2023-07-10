package com.example.demo1.controller;

import com.example.demo1.dto.RoomDTO;
import com.example.demo1.dto.StarDTO;
import com.example.demo1.service.StarService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/star")
@AllArgsConstructor
public class StarController {

    private final StarService starService;

    @PostMapping("/add")
    public void starRoom(@RequestBody StarDTO starDTO) {
        try {
            starService.starRoom(starDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete")
    public void deleteStar(@RequestBody StarDTO starDTO){
        try {
            starService.deleteStar(starDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}