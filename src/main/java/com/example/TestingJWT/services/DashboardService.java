package com.example.TestingJWT.services;


import com.example.TestingJWT.entities.RobotData;
import com.example.TestingJWT.repositories.RobotDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final RobotDataRepository robotDataRepository;

    public List<RobotData> getRobotDataByRobotId(String robotId) {
        return robotDataRepository.findByRobotId(robotId);

    }

    public Map<String, Object> calculateRiskStatus(String robotId) {
        List<RobotData> robotDataList = getRobotDataByRobotId(robotId);
        Map<String, Object> response = new HashMap<>();

        if (robotDataList.isEmpty()) {
            response.put("riskStatus", "No data available");
            response.put("averageRiskScore", 0.0);
            return response;
        }

        double averageRiskScore = robotDataList.stream()
                .mapToInt(RobotData::getRiskScore)
                .average()
                .orElse(0);

        String riskStatus = averageRiskScore > 60 ? "Risky" : "No threats";

        response.put("riskStatus", riskStatus);
        response.put("averageRiskScore", averageRiskScore);
        return response;
    }

}
