package com.example.TestingJWT.controllers;


import com.example.TestingJWT.entities.RobotData;
import com.example.TestingJWT.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/{robotId}")
    public ResponseEntity<List<RobotData>> getRobotDataByRobotId(@PathVariable String robotId) {
        return ResponseEntity.ok(dashboardService.getRobotDataByRobotId(robotId));
    }

    @GetMapping("/{robotId}/calculate-risk")
    public ResponseEntity<Map<String, Object>> calculateRiskStatus(@PathVariable String robotId) {
        Map<String, Object> response = dashboardService.calculateRiskStatus(robotId);
        return ResponseEntity.ok(response);
    }
}
