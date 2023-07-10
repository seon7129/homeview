package com.example.demo1.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StarDTO {

    private Long member_id;
    private Long room_id;

    public StarDTO(Long member_id, Long room_id){
        this.member_id = member_id;
        this.room_id = room_id;
    }
}