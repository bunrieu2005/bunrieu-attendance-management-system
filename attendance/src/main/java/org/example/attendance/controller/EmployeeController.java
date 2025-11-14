package org.example.attendance.controller;

import org.example.attendance.dto.EmployeeDTO;
import org.example.attendance.entity.Employee;
import org.example.attendance.mapper.EmployeeMapper;
import org.example.attendance.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        //converst dto before return
        List<EmployeeDTO> dtoList = EmployeeMapper.toDTOList(employees);
        return ResponseEntity.ok(dtoList);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        return employee.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getEmployeeByEmail(@PathVariable String email) {
        Optional<Employee> employee = employeeService.getEmployeeByEmail(email);
        return employee.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getEmployeeByStatus(@PathVariable String status) {
        List<Employee> employees = employeeService.getEmployeeByStatus(status);
        return ResponseEntity.ok(employees);
    }

    @PostMapping
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        Employee save = employeeService.saveEmployee(employee);
        return ResponseEntity.ok(save);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable long id, @RequestBody Employee employee) {
        Optional<Employee> exists = employeeService.getEmployeeById(id);
        if (exists.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employee.setId(id);
        Employee updated = employeeService.saveEmployee(employee);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable long id) {
        Optional<Employee> exists = employeeService.getEmployeeById(id);
        if (exists.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Complete delete employee id = " + id);
    }
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<?> getEmployeeByDepartment(@PathVariable Long departmentId) {
        List<Employee> list = employeeService.getDepartmentById(departmentId);
        return ResponseEntity.ok(list);
    }
}
