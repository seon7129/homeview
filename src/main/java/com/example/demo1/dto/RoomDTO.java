package com.example.demo1.dto;

import com.example.demo1.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class RoomDTO {
    private Long room_id;

    private String room_name;

    private Double latitude;

    private Double longitude;

    private Double total_score;

    public Room toEntity() {
        return Room.builder()
                .room_id(room_id)
                .room_name(room_name)
                .latitude(latitude)
                .longitude(longitude)
                .total_score(0.0)
                .build();
    }

}