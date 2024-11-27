package com.example.TestingJWT.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class MLDataDto {

    private String timestamp;

    @JsonProperty("risk_score")
    private double riskScore;

    @JsonProperty("risk_level")
    private String riskLevel;

    @JsonProperty("telemetry_data")
    private TelemetryData telemetryData;

    public int getDay() {
        LocalDateTime dateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
        return dateTime.getDayOfMonth();
    }

    @Data
    public static class TelemetryData {
        @JsonProperty("Acceleration")
        private Double acceleration;

        @JsonProperty("Altitude")
        private Double altitude;

        @JsonProperty("Deceleration")
        private Double deceleration;

        @JsonProperty("Latitude")
        private Double latitude;

        @JsonProperty("Longitude")
        private Double longitude;

        @JsonProperty("Pitch")
        private Double pitch;

        @JsonProperty("Roll")
        private Double roll;

        @JsonProperty("Speed")
        private Double speed;

        @JsonProperty("Yaw")
        private Double yaw;

        @JsonProperty("poses")
        private List<Pose> poses;
    }

    @Data
    public static class Pose {
        private Double px;
        private Double py;
        private Double theta;
    }
}
