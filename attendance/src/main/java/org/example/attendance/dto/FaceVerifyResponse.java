package org.example.attendance.dto;

import lombok.Data;

@Data
public class FaceVerifyResponse {

    private boolean success;
    private String message;
    private Integer employeeId;
    private Double score;       //  0.88)
}