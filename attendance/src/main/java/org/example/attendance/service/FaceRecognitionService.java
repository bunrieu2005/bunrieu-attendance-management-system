package org.example.attendance.service;

import org.example.attendance.dto.FaceEmbeddingResponse;
import org.example.attendance.dto.FaceVerifyResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FaceRecognitionService {


    private final String PYTHON_VERIFY_URL = "http://localhost:8000/face/verify";
    private final String PYTHON_EMBEDDING_URL = "http://localhost:8000/face/embedding";

    public FaceVerifyResponse verifyFace(MultipartFile file) {
        return callPythonApi(file, PYTHON_VERIFY_URL, FaceVerifyResponse.class);
    }

    public FaceEmbeddingResponse extractEmbedding(MultipartFile file) {
        return callPythonApi(file, PYTHON_EMBEDDING_URL, FaceEmbeddingResponse.class);
    }
    //Generic<T>:Generic Type
    private <T> T callPythonApi(MultipartFile file, String url, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();//RestTemplate:"mail carrier": Spring library Java call other APIs
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA); //call python :File not Word OK?

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new ByteArrayResource(file.getBytes()) { //byte array [0,1,0,1,0,0,..]
                @Override
                public String getFilename() {
                    return file.getOriginalFilename() != null ? file.getOriginalFilename() : "image.jpg";
                }
            });
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<T> response = restTemplate.postForEntity(url, requestEntity, responseType);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro call Python serve in: " + url);
            System.err.println("errro mess: " + e.getMessage());
            return null;
        }
    }
}