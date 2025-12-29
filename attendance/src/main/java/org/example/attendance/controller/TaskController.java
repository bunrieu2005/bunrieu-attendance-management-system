package org.example.attendance.controller;

import lombok.RequiredArgsConstructor;
import org.example.attendance.dto.CreateTaskDTO;
import org.example.attendance.entity.Task;
import org.example.attendance.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class TaskController {

    private final TaskService taskService;


    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDTO dto) {
        try {
            taskService.createTask(dto);
            Map<String, String> response = new HashMap<>();
            response.put("text", "Task assigned successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-tasks/{empId}")
    public ResponseEntity<List<Task>> getMyTasks(@PathVariable Long empId) {
        return ResponseEntity.ok(taskService.getMyTasks(empId));
    }

    @PutMapping("/status/{taskId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long taskId, @RequestParam String status) {
        try {
            taskService.updateStatus(taskId, status);
            Map<String, String> response = new HashMap<>();
            response.put("message", "update status successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
}