package org.example.attendance.repository;

import org.example.attendance.entity.LoginHistory;
import org.example.attendance.service.LoginHistoryService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  LoginHistoryRepo extends JpaRepository<LoginHistory,Long> {
    Optional<LoginHistory> getLoginHistoryById(long id);
    List<LoginHistory> getLoginByEmployeeId(long employeeId);
}
