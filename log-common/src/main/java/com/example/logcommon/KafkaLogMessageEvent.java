package com.example.logcommon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KafkaLogMessageEvent {

    private String serviceName;
    private LocalDateTime timestamp;
    private LogLevel logLevel;
    private String message;


}
