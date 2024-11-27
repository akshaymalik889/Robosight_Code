package com.example.TestingJWT.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RobotPose {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double px;
    private Double py;
    private Double theta;

    @ManyToOne
    @JoinColumn(name = "robot_data_id", nullable = false)
    @JsonIgnore
    private RobotData robotData;
}