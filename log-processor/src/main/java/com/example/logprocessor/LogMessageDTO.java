package com.example.logprocessor;

import com.example.logprocessor.entity.LogLevel;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogMessageDTO {
    private Long messageId;
    private String serverName;
    private LogLevel logLevel;
    private String message;
    private LocalDateTime timestamp;
}
