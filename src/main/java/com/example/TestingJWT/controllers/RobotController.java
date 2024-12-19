package com.example.TestingJWT.controllers;

import com.example.TestingJWT.entities.Robot;
import com.example.TestingJWT.services.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/robots")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class RobotController {

    private final RobotService robotService;

    @PostMapping
    public ResponseEntity<Robot> addRobot(@RequestBody Robot robot) {
        if (robot.getId() == null || robotService.getRobotById(robot.getId()) != null) {
            return ResponseEntity.badRequest().body(null); // Reject duplicate or null IDs
        }
        return ResponseEntity.ok(robotService.saveRobot(robot));
    }

    @GetMapping
    public ResponseEntity<List<Robot>> getAllRobots() {
        return ResponseEntity.ok(robotService.getAllRobots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Robot> getById(@PathVariable String id) {
        return ResponseEntity.ok(robotService.getRobotById(id));
    }
}
