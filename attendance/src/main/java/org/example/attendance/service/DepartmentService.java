package org.example.attendance.service;

import org.example.attendance.entity.Department;
import org.example.attendance.repository.DepartmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    @Autowired
    private DepartmentRepo departmentRepo;
    public List<Department> getAllDepartments(){
        return departmentRepo.findAll();
    }
    public Optional<Department> getDepartmentById(Long id){
        return departmentRepo.findById(id);
    }
    public Department saveDepartment(Department department){
        if(departmentRepo.existsByName(department.getName())){
            throw new RuntimeException("Department name already exists !!!");
        }
        return departmentRepo.save(department);
    }
    public void deleteDepartmentById(Long id){
        departmentRepo.deleteById(id);
    }

}
