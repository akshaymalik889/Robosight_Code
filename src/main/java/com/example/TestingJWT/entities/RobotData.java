package com.example.TestingJWT.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class RobotData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer day;
    private Integer riskScore;
    private String riskLevel;
    private Double acceleration;
    private Double altitude;
    private Double deceleration;
    private Double latitude;
    private Double longitude;
    private Double pitch;
    private Double roll;
    private Double speed;
    private Double yaw;

    @ManyToOne
    @JoinColumn(name = "robot_id", nullable = false)
    @JsonIgnore
    private Robot robot;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "robot_data_id") // Foreign key to link with RobotData
    private List<RobotPose> poses;
}
