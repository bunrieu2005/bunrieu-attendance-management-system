package org.example.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.LeaveRequestDTO;
import org.example.attendance.entity.Employee;
import org.example.attendance.entity.LeaveRequest;
import org.example.attendance.enums.LeaveStatus;
import org.example.attendance.mapper.LeaveRequestMapper;
import org.example.attendance.repository.EmployeeRepo;
import org.example.attendance.repository.LeaveRequestRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepo leaveRequestRepo;
    private final EmployeeRepo employeeRepo;
    private final LeaveRequestMapper leaveRequestMapper;

    // 1. Inject NotificationService
    private final NotificationService notificationService;

    @Transactional
    public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO dto) {
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        Employee emp = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + dto.getEmployeeId()));

        LeaveRequest request = leaveRequestMapper.toEntity(dto);
        request.setEmployee(emp);
        request.setStatus(LeaveStatus.PENDING);

        LeaveRequest savedRequest = leaveRequestRepo.save(request);
        return leaveRequestMapper.toDTO(savedRequest);
    }

    public List<LeaveRequestDTO> getRequestsByEmployeeId(Long employeeId) {
        List<LeaveRequest> list = leaveRequestRepo.findByEmployeeIdOrderByStartDateDesc(employeeId);
        return list.stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestDTO> getAllRequests() {
        List<LeaveRequest> list = leaveRequestRepo.findAllByOrderByStartDateDesc();
        return list.stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LeaveRequest updateStatus(Long id, LeaveStatus newStatus) {
        LeaveRequest request = leaveRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
        request.setStatus(newStatus);
        LeaveRequest savedRequest = leaveRequestRepo.save(request);

        Long employeeId = request.getEmployee().getId();
        String startDateStr = request.getStartDate().toString();

        String message = "";

        if (newStatus == LeaveStatus.APPROVED) {
            message = "Leave request from date " + startDateStr + " has been APPROVED.";
        } else if (newStatus == LeaveStatus.REJECTED) {
            message = "Leave request from date " + startDateStr + " has been REJECTED.";
        }
        if (!message.isEmpty()) {
            notificationService.sendNotification(employeeId, message);
        }

        return savedRequest;
    }

    @Transactional
    public LeaveRequest updateRequest(Long id, LeaveRequestDTO dto) {
        LeaveRequest request = leaveRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Cannot edit request that is not PENDING");
        }

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setReason(dto.getReason());
        request.setType(dto.getType());

        return leaveRequestRepo.save(request);
    }

    @Transactional
    public void deleteRequest(Long id) {
        LeaveRequest request = leaveRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Cannot delete request that is not PENDING");
        }

        leaveRequestRepo.delete(request);
    }
}