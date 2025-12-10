package org.example.attendance.controller;

import org.example.attendance.dto.DepartmentDTO;
import org.example.attendance.entity.Department;
import org.example.attendance.mapper.DepartmentMapper;
import org.example.attendance.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<Department> departments = departmentService.getAllDepartments();
        List<DepartmentDTO> dtoList= DepartmentMapper.toDTOList(departments);
        return ResponseEntity.ok(dtoList);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getDepartmentById(@PathVariable Long id) {
        Optional<Department> dept = departmentService.getDepartmentById(id);
        return dept.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Department> addDepartment(@RequestBody Department department) {
        Department saved = departmentService.saveDepartment(department);
        return ResponseEntity.ok(saved);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        Optional<Department> existing = departmentService.getDepartmentById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        department.setId(id);
        Department updated = departmentService.saveDepartment(department);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        Optional<Department> existing = departmentService.getDepartmentById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        departmentService.deleteDepartmentById(id);
        return ResponseEntity.ok("complete delete id = " + id);
    }
}
