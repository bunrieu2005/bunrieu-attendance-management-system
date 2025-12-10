package org.example.attendance.repository;

import org.example.attendance.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepo extends JpaRepository<LeaveRequest,Long> {
    List<LeaveRequest> findByEmployee_Id(Long id);
    List<LeaveRequest> findByStatus(String status);

    List<LeaveRequest> findByEmployeeIdOrderByStartDateDesc(Long employeeId);

    List<LeaveRequest> findAllByOrderByStartDateDesc();

}
