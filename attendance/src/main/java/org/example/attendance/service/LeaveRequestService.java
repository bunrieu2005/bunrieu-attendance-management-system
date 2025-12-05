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
    private final LeaveRequestMapper leaveRequestMapper; // Inject Mapper


    @Transactional
    public LeaveRequestDTO createLeaveRequest(LeaveRequestDTO dto) {
        Employee emp = employeeRepo.findById(dto.getEmployeeId())
                .orElseThrow(() -> new IllegalArgumentException("empployue not found: " + dto.getEmployeeId()));
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("start before end!!");
        }

        LeaveRequest request = leaveRequestMapper.toEntity(dto);

        request.setEmployee(emp);
        request.setStatus(LeaveStatus.PENDING);
        LeaveRequest savedRequest = leaveRequestRepo.save(request);

        return leaveRequestMapper.toDTO(savedRequest);
    }
    public List<LeaveRequestDTO> getAllRequests() {
        List<LeaveRequest> list = leaveRequestRepo.findAllByOrderByStartDateDesc();

        return list.stream()
                .map(leaveRequestMapper::toDTO)
                .collect(Collectors.toList());
    }
    // 3. duyet , tu choi
    public LeaveRequest updateStatus(Long id, LeaveStatus newStatus) {
        LeaveRequest request = leaveRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("no found leave request"));

        request.setStatus(newStatus);
        return leaveRequestRepo.save(request);
    }
    // 4.sua don
    public LeaveRequest updateRequest(Long id, LeaveRequestDTO dto) {
        LeaveRequest request = leaveRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("not fount leave request"));

        request.setStartDate(dto.getStartDate());
        request.setEndDate(dto.getEndDate());
        request.setReason(dto.getReason());
        request.setType(dto.getType());

        return leaveRequestRepo.save(request);
    }

    // 5. elete
    public void deleteRequest(Long id) {
        if (!leaveRequestRepo.existsById(id)) {
            throw new RuntimeException("not found for delete");
        }
        leaveRequestRepo.deleteById(id);
    }
}