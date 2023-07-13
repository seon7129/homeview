package com.example.demo1.dto.room;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RoomCheckDTO {

    private String old_address;
    private String building;

    public RoomCheckDTO(String old_address, String building) {
        this.old_address = old_address;
        this.building = building;
    }
}