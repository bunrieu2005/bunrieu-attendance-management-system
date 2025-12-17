package org.example.attendance.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.attendance.dto.FaceVerifyResponse;
import org.example.attendance.entity.Attendance;
import org.example.attendance.entity.Employee;
import org.example.attendance.repository.EmployeeRepo;
import org.example.attendance.service.CheckinService;
import org.example.attendance.service.FaceRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
// employee post request:
//   ↓
//   POST /api/attendances/check-in/{employeeId}
@RestController
@RequestMapping("/api/attendances")
public class CheckinController {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private FaceRecognitionService faceService;
    @Autowired
    private CheckinService checkinService;
    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<?> checkIn(@PathVariable Long employeeId,
                                     HttpServletRequest request,
                                     @RequestParam(defaultValue = "Click_IP") String method) { //POST /check-in/5?method=click_IP
        String ip = Optional.ofNullable(request.getHeader("X-Forwarded-For")) // get ip real //Nginx lấy IP từ TCP connection (không fake được)//User → Nginx (thêm IP thật) → Backend (check IP)
                .orElse(request.getRemoteAddr());
        Attendance result = checkinService.checkIn(employeeId, ip, method);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/check-out/{employeeId}")
    public ResponseEntity<?> checkOut(@PathVariable Long employeeId) {
        Attendance result = checkinService.checkOut(employeeId);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/check-in/face")
    public ResponseEntity<?> checkInByFace(@RequestParam("image") MultipartFile image, HttpServletRequest request) {
        Long recognizedId = null;
        try {
            FaceVerifyResponse result = faceService.verifyFace(image);
            if (!result.isSuccess()) {
                return ResponseEntity.status(401).body("Face not recognized");
            }
            recognizedId = Long.valueOf(result.getEmployeeId());
            String clientIp = getClientIp(request);
            Attendance attendance = checkinService.checkIn(recognizedId, clientIp, "FACE");
            return ResponseEntity.ok(attendance);

        } catch (IllegalStateException e) {
            return handleCheckinError(recognizedId, e);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/check-out/face")
    public ResponseEntity<?> checkOutByFace(@RequestParam("image") MultipartFile image) {
        try {
            FaceVerifyResponse result = faceService.verifyFace(image);
            if (!result.isSuccess()) {
                return ResponseEntity.status(401).body("Khuôn mặt không hợp lệ");
            }
            Long recognizedId = Long.valueOf(result.getEmployeeId());

            Attendance attendance = checkinService.checkOut(recognizedId);
            return ResponseEntity.ok(attendance);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .orElse(request.getRemoteAddr());
        if (ip != null && ip.contains(",")) ip = ip.split(",")[0].trim();
        return ip;
    }

    private ResponseEntity<?> handleCheckinError(Long recognizedId, Exception e) {
        Map<String, String> response = new HashMap<>();
        if (recognizedId != null) {
            Employee emp = employeeRepo.findById(recognizedId).orElse(null);
            String name = (emp != null) ? emp.getName() : "Unknown";
            response.put("employeeName", name);
        } else {
            response.put("employeeName", "Bạn");
        }
        response.put("message", "Hôm nay đã check-in rồi");
        return ResponseEntity.status(409).body(response);
    }
}
