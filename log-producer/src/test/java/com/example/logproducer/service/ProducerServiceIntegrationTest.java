package com.example.logproducer.service;

import com.example.logcommon.KafkaLogMessageEvent;
import com.example.logcommon.LogLevel;
import com.example.logproducer.dto.LogMessageDTO;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, controlledShutdown = true)
class ProducerServiceIntegrationTest {
    private static final String TEST_SERVICE_NAME = "test-service";
    private static final String TEST_TOPIC = TEST_SERVICE_NAME + "-logs-topic"; // Динамическое название топика

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaLogProducerService kafkaLogProducerService;

    private Consumer<String, KafkaLogMessageEvent> consumerServiceTest;

    @BeforeEach
    void setUp() {
        assertNotNull(embeddedKafkaBroker, "EmbeddedKafkaBroker не инициализирован!");
        embeddedKafkaBroker.addTopics(TEST_TOPIC);
        System.out.println("Доступные Топики: " + embeddedKafkaBroker.getTopics());

        // Конфигурация Kafka Consumer
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("group_consumer_test", "false", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ConsumerFactory<String, KafkaLogMessageEvent> cf = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(KafkaLogMessageEvent.class, false)
        );

        consumerServiceTest = cf.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumerServiceTest, TEST_TOPIC);
    }

    @Test
    void checks_that_the_producer_sends_correct_DTO_to_the_TOPIC() throws InterruptedException {
        LogMessageDTO testLogMessageDTO = new LogMessageDTO(
                TEST_SERVICE_NAME, // Теперь serviceName используется для формирования топика
                LogLevel.ERROR,
                "test-message",
                LocalDateTime.of(2023, 10, 1, 12, 34, 56)
        );

        System.out.println("Отправка сообщения в Kafka: " + testLogMessageDTO);
        kafkaLogProducerService.sendLogMessage(testLogMessageDTO);
        Thread.sleep(1000);

        System.out.println("Сообщение отправлено в Kafka");

        ConsumerRecord<String, KafkaLogMessageEvent> consumerRecord =
                KafkaTestUtils.getSingleRecord(consumerServiceTest, TEST_TOPIC);

        KafkaLogMessageEvent valueReceived = consumerRecord.value();

        assertEquals(TEST_SERVICE_NAME, valueReceived.getServiceName());
        assertEquals(LogLevel.ERROR, valueReceived.getLogLevel());
        assertEquals("test-message", valueReceived.getMessage());

        consumerServiceTest.close();
    }
}
