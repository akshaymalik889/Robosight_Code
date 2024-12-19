package com.example.TestingJWT.services;

import com.example.TestingJWT.entities.Robot;
import com.example.TestingJWT.repositories.RobotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RobotService {

    private final RobotRepository robotRepository;

    public Robot saveRobot(Robot robot) {
        return robotRepository.save(robot);
    }

    public Robot getRobotById(String id) {
        return robotRepository.findById(id).orElse(null);
    }

    public List<Robot> getAllRobots() {
        return robotRepository.findAll();
    }
}
