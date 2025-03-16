package com.example.logproducer.service.event;

import com.example.logproducer.service.LogLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaLogMessageEvent {

    private String serviceName;
    private LocalDateTime timestamp;
    private LogLevel logLevel;
    private String message;

}
