package org.example.attendance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "reason", length = 255)
    private String reason;
    @Column(name = "type", length = 30)
    private String type;
    @Column(name = "status", length = 20)
    private String status;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="employee_id",referencedColumnName = "id")
    private Employee employee;
}
