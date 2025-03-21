package com.example.logprocessor.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "log_statistic")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogStatistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "log_date")
    private LocalDateTime logDate;

    @JoinColumn(name = "level_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private LogLevel logLevel;

    @Column(name = "count")
    private int countLogLevel;
}
