package org.example.attendance.dto;

import lombok.Data;
import java.util.List;

@Data
public class FaceEmbeddingResponse {
    private boolean success;
    private String message;
    private List<Double> embedding; // 512 chieu face
}