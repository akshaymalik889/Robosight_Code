package com.example.TestingJWT.services;

import com.example.TestingJWT.entities.RobotData;
import com.example.TestingJWT.repositories.RobotDataRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RobotDataService {

    private final RobotDataRepository robotDataRepository;

    @Transactional
    public List<RobotData> saveAll(List<RobotData> robotDataList) {
        return robotDataRepository.saveAll(robotDataList);
    }

    @Transactional
    public List<RobotData> getDataByRobotId(Long robotId) {
        return robotDataRepository.findByRobotId(robotId);
    }

    @Transactional
    public void deleteDataByRobotId(Long robotId) {
        robotDataRepository.deleteByRobotId(robotId);
    }
}
