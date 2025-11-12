package org.example.attendance.repository;

import org.example.attendance.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepo extends JpaRepository<LeaveRequest,Long> {
    List<LeaveRequest> id(Long id);
    List<LeaveRequest> findByEmployeeId(Long id);
   List<LeaveRequest> getLeaveRequestByStatus(String status);
}
