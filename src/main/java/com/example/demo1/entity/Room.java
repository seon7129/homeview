package com.example.demo1.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_id")
    private Long room_id;

    @Column(nullable = false, length = 50)
    private String building;
    @Column(nullable = false, length = 500)
    private Double latitude;
    @Column(nullable = false, length = 500)
    private Double longitude;
    private String new_address;
    private String old_address;
    private String sido;
    private String sigungu;
    private String dong;

    @Builder
    public Room(Long room_id, String building, Double latitude, Double longitude, /*Double total_score,*/
                String new_address, String old_address, String sido, String sigungu, String dong){
        this.room_id = room_id;
        this.building = building;
        this.latitude = latitude;
        this.longitude = longitude;
//        this.total_score = total_score;
        this.new_address = new_address;
        this.old_address = old_address;
        this.sido = sido;
        this.sigungu = sigungu;
        this.dong = dong;
    }
}