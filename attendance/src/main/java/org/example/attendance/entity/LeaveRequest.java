package org.example.attendance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.example.attendance.enums.LeaveStatus;
import org.example.attendance.enums.LeaveType;

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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType type;        //get only data this enum
   @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name="employee_id",referencedColumnName = "id")
    private Employee employee;
}
