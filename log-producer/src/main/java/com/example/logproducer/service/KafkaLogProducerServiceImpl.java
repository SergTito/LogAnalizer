package com.example.logproducer.service;

import com.example.logproducer.dto.LogMessageDTO;
import com.example.logproducer.service.event.KafkaLogMessageEvent;
import org.apache.kafka.clients.producer.RecordMetadata;
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
        String topicName = logMessageDTO.getServiceName() + "-logs-topic";

        KafkaLogMessageEvent kafkaLogEvent = new KafkaLogMessageEvent(
                logMessageDTO.getServiceName(),
                logMessageDTO.getTimestamp(),
                logMessageDTO.getLogLevel(),
                logMessageDTO.getMessage()
        );

        CompletableFuture<SendResult<String, KafkaLogMessageEvent>> future =
                kafkaLogTemplate.send(topicName, logMessageDTO.getServiceName(), kafkaLogEvent);

        future.thenAccept(result -> {
            RecordMetadata metadata = result.getRecordMetadata();
            LOGGER.info("Сообщение отправлено в topic: {}, partition: {}, offset: {}, key: {}",
                    topicName, metadata.partition(), metadata.offset(), logMessageDTO.getServiceName());
        }).exceptionally(ex -> {
            LOGGER.error("Не удалось отправить сообщение в Kafka. Service: {}, Error: {} [{}]",
                    logMessageDTO.getServiceName(), ex.getClass().getSimpleName(), ex.getMessage(), ex);
            return null;
        });


    }
}
