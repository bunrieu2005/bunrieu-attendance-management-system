package org.example.attendance.controller;

import org.example.attendance.dto.EmployeeDTO;
import org.example.attendance.dto.EmployeeDetailDTO;
import org.example.attendance.entity.Department;
import org.example.attendance.entity.Employee;
import org.example.attendance.mapper.EmployeeMapper;
import org.example.attendance.repository.DepartmentRepo;
import org.example.attendance.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentRepo departmentRepo;
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        List<EmployeeDTO> dtoList = EmployeeMapper.toDTOList(employees);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(emp -> ResponseEntity.ok(EmployeeMapper.toDTO(emp)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getEmployeeByEmail(@PathVariable String email) {
        Optional<Employee> employee = employeeService.getEmployeeByEmail(email);
        return employee.map(emp -> ResponseEntity.ok(EmployeeMapper.toDTO(emp)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getEmployeeByStatus(@PathVariable String status) {
        List<Employee> employees = employeeService.getEmployeeByStatus(status);
        List<EmployeeDTO> dtos = EmployeeMapper.toDTOList(employees);
        return ResponseEntity.ok(dtos);
    }
    @GetMapping("/{id}/profile")
    public ResponseEntity<?> getEmployeeProfile(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee
                .map(emp -> ResponseEntity.ok(EmployeeMapper.toDetailDTO(emp)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/email/{email}/profile")
    public ResponseEntity<?> getEmployeeProfileByEmail(@PathVariable String email) {
        Optional<Employee> employee = employeeService.getEmployeeByEmail(email);
        return employee
                .map(emp -> ResponseEntity.ok(EmployeeMapper.toDetailDTO(emp)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<?> getEmployeeByDepartment(@PathVariable Long departmentId) {
        List<Employee> list = employeeService.getDepartmentById(departmentId);
        List<EmployeeDTO> dtos = EmployeeMapper.toDTOList(list);
        return ResponseEntity.ok(dtos);
    }
  // add employee for bcyrps
    @PostMapping
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeDTO dto) {
        try {

            Employee employee = EmployeeMapper.toEntity(dto);

            if (dto.getDepartmentId() != null) {
                Department department = departmentRepo.findById(dto.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                employee.setDepartment(department);
            } else {
                employee.setDepartment(null);
            }

            String rawPassword = (dto.getPassword() != null && !dto.getPassword().isEmpty())
                    ? dto.getPassword()
                    : "123";
            employee.setPassword(passwordEncoder.encode(rawPassword));
            Employee saved = employeeService.saveEmployee(employee);
            EmployeeDTO responseDto = EmployeeMapper.toDTO(saved);
            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeDTO dto) {
        try {
            Optional<Employee> existingOpt = employeeService.getEmployeeById(id);
            if (existingOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Employee existing = existingOpt.get();

            existing.setName(dto.getName());
            existing.setEmail(dto.getEmail());
            existing.setDob(dto.getDob());
            existing.setGender(dto.getGender());
            existing.setRole(dto.getRole());
            existing.setStatus(dto.getStatus());
            existing.setHireDate(dto.getHireDate());

            if (dto.getDepartmentId() != null) {
                Department department = departmentRepo.findById(dto.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                existing.setDepartment(department);
                System.out.println("Department set: " + department.getName());
            } else {
                System.out.println("Department giữ nguyên: " +
                        (existing.getDepartment() != null ? existing.getDepartment().getName() : "null"));
            }

            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                existing.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            Employee updated = employeeService.saveEmployee(existing);

            System.out.println("Saved! Department: " +
                    (updated.getDepartment() != null ? updated.getDepartment().getName() : "null"));

            Employee reloaded = employeeService.getEmployeeById(updated.getId())
                    .orElse(updated);

            EmployeeDTO responseDto = EmployeeMapper.toDTO(reloaded);
            System.out.println("Response DTO departmentId: " + responseDto.getDepartmentId());

            return ResponseEntity.ok(responseDto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        Optional<Employee> exists = employeeService.getEmployeeById(id);
        if (exists.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
    }
}