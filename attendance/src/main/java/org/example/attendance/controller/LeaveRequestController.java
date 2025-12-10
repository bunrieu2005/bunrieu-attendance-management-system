package org.example.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.LeaveRequestDTO;
import org.example.attendance.enums.LeaveStatus;
import org.example.attendance.mapper.LeaveRequestMapper;
import org.example.attendance.service.LeaveRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-requests")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final LeaveRequestMapper leaveRequestMapper;
    // user create leave request:
    // POST: http://localhost:8080/api/leave-requests
    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody LeaveRequestDTO dto) {
        try {
            LeaveRequestDTO created = leaveRequestService.createLeaveRequest(dto);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("erro srve: " + e.getMessage());
        }
    }
    // list:amin
    // GET: http://localhost:8080/api/leave-requests
    @GetMapping
    public ResponseEntity<List<LeaveRequestDTO>> getAllRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllRequests());
    }
    // update status: admin
    // URL: PUT /api/leave-requests/{id}/status?status=APPROVED
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam LeaveStatus status) {
        try {
            LeaveRequestDTO updated = leaveRequestMapper.toDTO(
                    leaveRequestService.updateStatus(id, status)
            );
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
    // edit api
    // PUT: http://localhost:8080/api/leave-requests/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable Long id, @RequestBody LeaveRequestDTO dto) {
        try {
            LeaveRequestDTO updated = leaveRequestMapper.toDTO(
                    leaveRequestService.updateRequest(id, dto)
            );
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }

    // delete api
    // DELETE: http://localhost:8080/api/leave-requests/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long id) {
        try {
            leaveRequestService.deleteRequest(id);
            return ResponseEntity.ok("delete sucssece");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("erro: " + e.getMessage());
        }
}
    // GET: http://localhost:8080/api/leave-requests/employee/{employeeId}
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequestDTO>> getRequestsByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(leaveRequestService.getRequestsByEmployeeId(employeeId));
    }
}