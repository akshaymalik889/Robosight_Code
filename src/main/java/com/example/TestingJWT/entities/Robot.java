package com.example.TestingJWT.entities;

import com.example.TestingJWT.entities.enums.Task;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Robot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String manufacturer;

    @Enumerated(EnumType.STRING)
    private Task task;

    @OneToMany(mappedBy = "robot", cascade = CascadeType.ALL)
    private List<RobotData> robotDataList;


}
