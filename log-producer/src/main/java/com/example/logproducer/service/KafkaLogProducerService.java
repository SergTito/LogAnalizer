package com.example.logproducer.service;

import com.example.logproducer.dto.LogMessageDTO;

public interface KafkaLogProducerService {
    void sendLogMessage(LogMessageDTO logMessageDTO);
}
