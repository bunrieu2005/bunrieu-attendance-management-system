package org.example.attendance.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.attendance.entity.Attendance;
import org.example.attendance.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
// employee post request:
//   ↓
//   POST /api/attendances/check-in/{employeeId}
@RestController
@RequestMapping("/api/attendances")
public class CheckinController {
    @Autowired
    private CheckinService checkinService;
    @PostMapping("/check-in/{employeeId}")
    public ResponseEntity<?> checkIn(@PathVariable Long employeeId,
                                     HttpServletRequest request,
                                     @RequestParam(defaultValue = "QR_IP") String method) {
        String ip = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .orElse(request.getRemoteAddr());
        Attendance result = checkinService.checkIn(employeeId, ip, method);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/check-out/{employeeId}")
    public ResponseEntity<?> checkOut(@PathVariable Long employeeId) {
        Attendance result = checkinService.checkOut(employeeId);
        return ResponseEntity.ok(result);
    }
}
