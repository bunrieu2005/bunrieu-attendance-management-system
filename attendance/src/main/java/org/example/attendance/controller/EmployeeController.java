package org.example.attendance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.attendance.dto.EmployeeDTO;
import org.example.attendance.dto.EmployeeDetailDTO;
import org.example.attendance.dto.FaceEmbeddingResponse;
import org.example.attendance.entity.Department;
import org.example.attendance.entity.Employee;
import org.example.attendance.mapper.EmployeeMapper;
import org.example.attendance.repository.DepartmentRepo;
import org.example.attendance.repository.EmployeeRepo;
import org.example.attendance.service.EmployeeService;
import org.example.attendance.service.FaceRecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @Autowired
    private EmployeeRepo employeeRepo;

    @Autowired
    private FaceRecognitionService faceService;
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
    private String saveImage(MultipartFile file, Long empId) throws Exception {
        String uploadDir = "src/main/resources/static/images/avatars/";
        String fileName = empId + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        return "/images/avatars/" + fileName;
    }
  // add employee for bcyrps
  @PostMapping(consumes = { "multipart/form-data" })
  public ResponseEntity<?> addEmployee(
          @ModelAttribute EmployeeDTO dto,
          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
      try {
          Employee employee = EmployeeMapper.toEntity(dto);
          if (dto.getDepartmentId() != null) {
              Department department = departmentRepo.findById(dto.getDepartmentId())
                      .orElseThrow(() -> new RuntimeException("Department not found"));
              employee.setDepartment(department);
          } else {
              employee.setDepartment(null);
          }

          String rawPassword = (dto.getPassword() != null && !dto.getPassword().isEmpty()) ? dto.getPassword() : "123";
          employee.setPassword(passwordEncoder.encode(rawPassword));

          Employee saved = employeeService.saveEmployee(employee);


          if (imageFile != null && !imageFile.isEmpty()) {
              String imageUrl = saveImage(imageFile, saved.getId());
              saved.setImage(imageUrl);
              employeeService.saveEmployee(saved);
          }

          return ResponseEntity.ok(EmployeeMapper.toDTO(saved));

      } catch (Exception e) {
          e.printStackTrace();
          return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
      }
  }
    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<?> updateEmployee(
            @PathVariable Long id,
            @ModelAttribute EmployeeDTO dto,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Optional<Employee> existingOpt = employeeService.getEmployeeById(id);
            if (existingOpt.isEmpty()) return ResponseEntity.notFound().build();

            Employee existing = existingOpt.get();

            existing.setName(dto.getName());
            existing.setEmail(dto.getEmail());
            existing.setDob(dto.getDob());
            existing.setGender(dto.getGender());
            existing.setRole(dto.getRole());
            existing.setStatus(dto.getStatus());
            existing.setHireDate(dto.getHireDate());

            if (dto.getDepartmentId() != null) {
                Department department = departmentRepo.findById(dto.getDepartmentId()).orElse(null);
                existing.setDepartment(department);
            }

            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                existing.setPassword(passwordEncoder.encode(dto.getPassword()));
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = saveImage(imageFile, existing.getId());
                existing.setImage(imageUrl);
            }

            Employee updated = employeeService.saveEmployee(existing);
            return ResponseEntity.ok(EmployeeMapper.toDTO(updated));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
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

    @PostMapping("/{id}/face")
    public ResponseEntity<?> registerFace(@PathVariable Long id, @RequestParam("image") MultipartFile image) {
        try {
            Employee employee = employeeRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("not found employee id: " + id));

            FaceEmbeddingResponse response = faceService.extractEmbedding(image);

            if (response == null || !response.isSuccess()) {
                return ResponseEntity.badRequest().body("erro: not found face in image");
            }

            ObjectMapper mapper = new ObjectMapper();
            String embeddingJson = mapper.writeValueAsString(response.getEmbedding());
            employee.setFaceEmbedding(embeddingJson);
            employeeRepo.save(employee);

            return ResponseEntity.ok("complete update face for employee: " + employee.getName());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(" server erro: " + e.getMessage());
        }
    }
}