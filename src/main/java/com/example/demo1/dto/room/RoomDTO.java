package com.example.demo1.dto.room;

import com.example.demo1.entity.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class RoomDTO {
    private Long room_id;

    private String building;

    private Double latitude;

    private Double longitude;

//    private Double total_score;

    private String new_address;
    private String old_address;
    private String sido;
    private String sigungu;
    private String dong;

    public Room toEntity() {
        return Room.builder()
                .room_id(room_id)
                .building(building)
                .latitude(latitude)
                .longitude(longitude)
                //.total_score(0.0)
                .new_address(new_address)
                .old_address(old_address)
                .sido(sido)
                .sigungu(sigungu)
                .dong(dong)
                .build();
    }

}