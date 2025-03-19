package com.example.logproducer.controller;

import com.example.logproducer.dto.LogMessageDTO;
import com.example.logproducer.service.KafkaLogProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
@Tag(
        name = "Log Producer API",
        description = "API для отправки логов в Kafka. " +
                      "Принимает логи в формате JSON и отправляет их в Kafka для дальнейшей обработки"
)
public class ProducerController {

    private static final Logger logger = LoggerFactory.getLogger(ProducerController.class);

    private final KafkaLogProducerService logProducerService;

    public ProducerController(KafkaLogProducerService logProducerService) {
        this.logProducerService = logProducerService;
    }


    @PostMapping(value = "/log-producer")
    @Operation(
            summary = "Отправить лог в Kafka",
            description = "Принимает лог в формате JSON, преобразует его в объект и отправляет в Kafka для дальнейшей обработки."
    )
    public ResponseEntity<String> sendLog(
            @Parameter(
                    description = "DTO лог-сообщения. Содержит информацию о сервисе, уровне лога, сообщении и временной метке.",
                    required = true
            )
            @Valid @RequestBody LogMessageDTO dto
    ) {
        logger.info("Received log message from service: {}", dto.getServiceName());

        try {
            logProducerService.sendLogMessage(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Сообщение логов отправлено в Kafka");
        } catch (IllegalArgumentException e) {
            logger.error("Недействительный запрос: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Не удалось отправить сообщение журнала: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send log message: " + e.getMessage());
        }
    }
}
