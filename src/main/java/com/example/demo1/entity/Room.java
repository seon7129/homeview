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
    private String room_name;

    @Column(nullable = false, length = 500)
    private Double latitude;

    @Column(nullable = false, length = 500)
    private Double longitude;

    @Column(nullable = false, length = 500)
    private Double total_score;

    @Builder
    public Room(Long room_id, String room_name, Double latitude, Double longitude, Double total_score){
        this.room_id = room_id;
        this.room_name = room_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.total_score = total_score;
    }
}