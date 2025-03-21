//package com.example.logprocessor.handler;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ProducerCreatedEventHandler {
//
//    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
//
//    @KafkaListener(topicPattern = ".*-logs-topic", groupId = "log-processor-group")
//    public void handle(KafkaLogMessageEvent kafkaLogMessageEvent){
//        LOGGER.info("Получено сообщение из Kafka:{}",kafkaLogMessageEvent.getTitle());
//    }
//
//}
