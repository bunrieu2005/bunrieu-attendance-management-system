package org.example.attendance.controller;

import org.example.attendance.entity.LeaveRequest;
import org.example.attendance.service.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/leaverequests")
public class LeaveRequestController {
    @Autowired
    private LeaveRequestService leaveRequestService;
    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests() {
        List<LeaveRequest> leaveRequests = leaveRequestService.getAllLeaveRequest();
        return ResponseEntity.ok().body(leaveRequests);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable Long id) {
        Optional<LeaveRequest> leaveRequest = leaveRequestService.getLeaveRequestById(id);
        return leaveRequest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getAttendanceByEmployeeId(@PathVariable Long employeeId) {
        List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestByEmployeeId(employeeId);
                return  ResponseEntity.ok().body(leaveRequests);
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequestsByStatus(@PathVariable String status){
        List<LeaveRequest> list = leaveRequestService.getByStatus(status);
        return ResponseEntity.ok(list);
    }
    @PostMapping
    public ResponseEntity<?> addLeaveRequest(@RequestBody LeaveRequest leaveRequest){
        LeaveRequest save =leaveRequestService.saveLeaveRequest(leaveRequest);
        return ResponseEntity.ok().body(save);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLeaveRequest(@PathVariable Long id){
        leaveRequestService.deleteLeaveRequestById(id);
        return ResponseEntity.ok("Deleted leave request id = " + id);
    }

}
