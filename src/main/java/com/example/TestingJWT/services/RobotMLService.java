package com.example.TestingJWT.services;

import com.example.TestingJWT.dtos.MLDataDto;
import com.example.TestingJWT.entities.Robot;
import com.example.TestingJWT.entities.RobotData;
import com.example.TestingJWT.entities.RobotPose;
import com.example.TestingJWT.repositories.RobotRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RobotMLService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RobotDataService robotDataService;
    private final RobotRepository robotRepository;

    public RobotMLService(RestTemplate restTemplate, ObjectMapper objectMapper, RobotDataService robotDataService, RobotRepository robotRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.robotDataService = robotDataService;
        this.robotRepository = robotRepository;
    }

    public List<RobotData> getAndSaveRiskData(Long robotId) {
        String mlApiUrl = "http://localhost:8000/api/telemetry/get_all";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Long> request = new HttpEntity<>(robotId, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    mlApiUrl, HttpMethod.GET, request, String.class
            );

            log.info("Raw ML Model Response: {}", response.getBody());

            System.out.println("ML RESPONSE"+response.getBody());

            List<MLDataDto> mlDataDtos = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<MLDataDto>>() {}
            );

            System.out.println("ML DATA DTO"+mlDataDtos);

            Robot robot = robotRepository.findById(robotId)
                    .orElseThrow(() -> new RuntimeException("Robot not found with ID: " + robotId));

            List<RobotData> robotDataList = mlDataDtos.stream().map(dto -> {
                RobotData robotData = new RobotData();
                robotData.setDay(dto.getDay());
                robotData.setRiskScore((int) dto.getRiskScore());
                robotData.setRiskLevel(dto.getRiskLevel());
                robotData.setRobot(robot);

                MLDataDto.TelemetryData telemetry = dto.getTelemetryData();
                if (telemetry != null) {
                    robotData.setAcceleration(telemetry.getAcceleration());
                    robotData.setAltitude(telemetry.getAltitude());
                    robotData.setDeceleration(telemetry.getDeceleration());
                    robotData.setLatitude(telemetry.getLatitude());
                    robotData.setLongitude(telemetry.getLongitude());
                    robotData.setPitch(telemetry.getPitch());
                    robotData.setRoll(telemetry.getRoll());
                    robotData.setSpeed(telemetry.getSpeed());
                    robotData.setYaw(telemetry.getYaw());

                    List<RobotPose> poseList = telemetry.getPoses().stream().map(poseDto -> {
                        RobotPose pose = new RobotPose();
                        pose.setPx(poseDto.getPx());
                        pose.setPy(poseDto.getPy());
                        pose.setTheta(poseDto.getTheta());
                        pose.setRobotData(robotData); // Link each pose to the current RobotData
                        return pose;
                    }).collect(Collectors.toList());
                    robotData.setPoses(poseList);
                }
                return robotData;
            }).collect(Collectors.toList());

            return robotDataService.saveAll(robotDataList);

        } catch (Exception e) {
            log.error("Error retrieving and saving risk data from ML model", e);
            throw new RuntimeException("Error retrieving and saving risk data from ML model", e);
        }
    }

    public List<RobotData> processTelemetryData(Long robotId, String telemetryJson) {
        String mlApiUrl = "http://localhost:8000/api/telemetry/push";

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(telemetryJson, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    mlApiUrl, HttpMethod.POST, request, String.class
            );

            List<MLDataDto> mlDataDtos = objectMapper.readValue(
                    response.getBody(), new TypeReference<List<MLDataDto>>() {}
            );

            Robot robot = robotRepository.findById(robotId)
                    .orElseThrow(() -> new RuntimeException("Robot not found with ID: " + robotId));

            List<RobotData> robotDataList = mlDataDtos.stream().map(dto -> {
                RobotData robotData = new RobotData();
                robotData.setDay(dto.getDay());
                robotData.setRiskScore((int) dto.getRiskScore());
                robotData.setRiskLevel(dto.getRiskLevel());
                robotData.setRobot(robot);

                MLDataDto.TelemetryData telemetry = dto.getTelemetryData();
                if (telemetry != null) {
                    robotData.setAcceleration(telemetry.getAcceleration());
                    robotData.setAltitude(telemetry.getAltitude());
                    robotData.setDeceleration(telemetry.getDeceleration());
                    robotData.setLatitude(telemetry.getLatitude());
                    robotData.setLongitude(telemetry.getLongitude());
                    robotData.setPitch(telemetry.getPitch());
                    robotData.setRoll(telemetry.getRoll());
                    robotData.setSpeed(telemetry.getSpeed());
                    robotData.setYaw(telemetry.getYaw());

                    List<RobotPose> poseList = telemetry.getPoses().stream().map(poseDto -> {
                        RobotPose pose = new RobotPose();
                        pose.setPx(poseDto.getPx());
                        pose.setPy(poseDto.getPy());
                        pose.setTheta(poseDto.getTheta());
                        return pose;
                    }).collect(Collectors.toList());
                    robotData.setPoses(poseList);
                }
                return robotData;
            }).collect(Collectors.toList());

            return robotDataService.saveAll(robotDataList);

        } catch (Exception e) {
            log.error("Error processing telemetry data", e);
            throw new RuntimeException("Error processing telemetry data", e);
        }
    }
}
