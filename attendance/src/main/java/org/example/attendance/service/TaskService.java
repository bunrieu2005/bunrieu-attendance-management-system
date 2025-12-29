package org.example.attendance.service;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.CreateTaskDTO;
import org.example.attendance.entity.Employee;
import org.example.attendance.entity.Task;
import org.example.attendance.repository.EmployeeRepo;
import org.example.attendance.repository.TaskRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepo taskRepo;
    private final EmployeeRepo employeeRepo;
    private final NotificationService notificationService;

    @Transactional
    public void createTask(CreateTaskDTO dto) {
        if ("DEPARTMENT".equals(dto.getTargetType())) {
            List<Employee> employees = employeeRepo.findByDepartmentId(dto.getTargetId());
            for (Employee emp : employees) {
                saveTaskAndNotify(emp, dto);
            }
        } else {
            Employee emp = employeeRepo.findById(dto.getTargetId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            saveTaskAndNotify(emp, dto);
        }
    }

    private void saveTaskAndNotify(Employee emp, CreateTaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setContent(dto.getContent());
        task.setEmployee(emp);
        task.setDeadline(dto.getDeadline());
        task.setStatus("PENDING");

        Task savedTask = taskRepo.save(task);

        notificationService.createNotification(
                emp.getId(),
                "new Task: " + dto.getTitle(),
                "you have just been assigned a new task. Just a moment: " + dto.getDeadline(),
                "TASK",
                savedTask.getId()
        );
    }

    public List<Task> getMyTasks(Long empId) {
        return taskRepo.findByEmployeeIdOrderByCreatedAtDesc(empId);
    }

    public void updateStatus(Long taskId, String status) {
        Task task = taskRepo.findById(taskId).orElseThrow();
        task.setStatus(status);
        taskRepo.save(task);
    }
}