package com.example.logproducer.service;

import com.example.logproducer.dto.LogMessageDTO;
import com.example.logproducer.service.event.KafkaLogMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Service
public class KafkaLogProducerServiceImpl implements KafkaLogProducerService {


    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaLogProducerServiceImpl.class);
    private final KafkaTemplate<String, KafkaLogMessageEvent> kafkaLogTemplate;

    public KafkaLogProducerServiceImpl(KafkaTemplate<String, KafkaLogMessageEvent> kafkaTemplate) {
        this.kafkaLogTemplate = kafkaTemplate;
    }


    @Override
    public void sendLogMessage(LogMessageDTO logMessageDTO) {

        KafkaLogMessageEvent kafkaLogEvent = new KafkaLogMessageEvent(
                logMessageDTO.getServiceName(),
                logMessageDTO.getTimestamp(),
                logMessageDTO.getLogLevel(),
                logMessageDTO.getMessage()
        );

        CompletableFuture<SendResult<String, KafkaLogMessageEvent>> future =
                kafkaLogTemplate.send(
                        "kafka-topic", logMessageDTO.getServiceName(), kafkaLogEvent);

        future.thenAccept(result ->
                LOGGER.info("Сообщение отправлено в topic: kafka-topic, key: {}", logMessageDTO.getServiceName())
        ).exceptionally(ex -> {
            LOGGER.error("Не удалось отправить сообщение в Kafka. Service: {}, Error: {}",
                    logMessageDTO.getServiceName(), ex.getMessage(), ex);
            return null;
        });

    }
}
