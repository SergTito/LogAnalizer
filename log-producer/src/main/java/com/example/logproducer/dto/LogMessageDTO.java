package com.example.logproducer.dto;

import com.example.logproducer.service.LogLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogMessageDTO {

    @Schema(
            description = "Название сервиса, отправившего лог",
            example = "user-service"
    )
    @NotBlank(message = "Имя сервиса не может быть пустым")
    private String serviceName;

    @Schema(
            description = "Уровень лога (например, INFO, WARN, ERROR)",
            example = "ERROR"
    )
    @NotNull(message = "Уровень лога не может быть null")
    private LogLevel logLevel;

    @Schema(
            description = "Текст лог-сообщения",
            example = "Не удалось подключиться к базе данных"
    )
    @NotBlank(message = "Сообщение не может быть пустым")
    private String message;

    @Schema(
            description = "Временная метка лога",
            example = "2023-10-01T12:34:56"
    )
    @NotNull(message = "Временная метка не может быть null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;



}
