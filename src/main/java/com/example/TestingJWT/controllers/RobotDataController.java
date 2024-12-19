package com.example.TestingJWT.controllers;

import com.example.TestingJWT.entities.Robot;
import com.example.TestingJWT.entities.RobotData;
import com.example.TestingJWT.services.RobotDataService;
import com.example.TestingJWT.services.RobotMLService;
import com.example.TestingJWT.services.RobotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/upload-data")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class RobotDataController {

    private final RobotDataService robotDataService;
    private final RobotService robotService;
    private final RobotMLService robotMLService;

    @GetMapping("/{robotId}")
    public ResponseEntity<?> addRobotData(@PathVariable String robotId) {
        Robot robot = robotService.getRobotById(robotId);
        if (robot == null) {
            return ResponseEntity.badRequest().body("Robot with ID " + robotId + " not found.");
        }

        try {
            robotDataService.deleteDataByRobotId(robotId);

            List<RobotData> robotDataList = robotMLService.getAndSaveRiskData(robotId);

            return ResponseEntity.ok(robotDataList);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing data for Robot ID " + robotId + ": " + e.getMessage());
        }
    }

    @PostMapping("/{robotId}")
    public ResponseEntity<?> uploadTelemetryData(@PathVariable String robotId, @RequestBody String telemetryJson) {
        Robot robot = robotService.getRobotById(robotId);
        System.out.println("robot details by IDDDDDD:"+robot);
        if (robot == null) {
            return ResponseEntity.badRequest().body("Robot with ID " + robotId + " not found.");
        }

        try {
            robotDataService.deleteDataByRobotId(robotId);

            List<RobotData> robotDataList = robotMLService.processTelemetryData(robotId, telemetryJson);

            return ResponseEntity.ok(robotDataList);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing telemetry data: " + e.getMessage());
        }
    }
}
