package org.example.attendance.service;

import org.example.attendance.entity.Department;
import org.example.attendance.entity.Employee;
import org.example.attendance.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepo employeeRepo;
    public List<Employee> getAllEmployees(){
        return employeeRepo.findAll();
    }
    public Optional<Employee> getEmployeeById(Long id){
        return employeeRepo.findById(id);
    }
    public Optional<Employee> getEmployeeByEmail(String email){
        return employeeRepo.findByEmail(email);
    }
    public List<Employee> getEmployeeByStatus(String status){
        return employeeRepo.findByStatus(status);
    }
    public List<Employee> getDepartmentById(Long departmentId){
        return employeeRepo.findByDepartment_Id(departmentId);
    }
    public Employee saveEmployee(Employee employee){
        return employeeRepo.save(employee);
    }
    public void deleteEmployee(Long id){
        employeeRepo.deleteById(id);
    }
    public long  countAll(){
        return employeeRepo.count();
    }
}
