package org.example.attendance.repository;

import org.example.attendance.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
    List<Task> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);
}