package com.example.TestingJWT.repositories;

import com.example.TestingJWT.entities.RobotData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RobotDataRepository extends JpaRepository<RobotData, Long> {

    List<RobotData> findByRobotId(Long robotId);

    void deleteByRobotId(Long robotId);
}
