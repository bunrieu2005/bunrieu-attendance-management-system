package org.example.attendance.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "system_configs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfig {
    @Id
    @Column(name = "config_key")
    private String key;

    @Column(name = "config_value")
    private String value;

    private String description;
}