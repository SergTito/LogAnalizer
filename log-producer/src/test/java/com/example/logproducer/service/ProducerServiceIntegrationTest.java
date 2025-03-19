package com.example.logproducer.service;

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
    private static final String TEST_TOPIC = "test-topic-logs-topic";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private KafkaLogProducerService kafkaLogProducerService;

    @BeforeEach
    void setUp() {
        assertNotNull(embeddedKafkaBroker, "Inceddedkafkabroker не инициализируется!");
        embeddedKafkaBroker.addTopics(TEST_TOPIC);
        System.out.println("Доступные Топики: " + embeddedKafkaBroker.getTopics());
    }

    @Test
    void checks_that_the_producer_is_sending_correct_DTO_to_the_TOPIC_EXAMPLE_EXTERNE() throws InterruptedException {

        LogMessageDTO testLogMessageDTO = new LogMessageDTO(
                "test-topic",
                LogLevel.ERROR,
                "test-message",
                LocalDateTime.of(2023, 10, 01, 12, 34, 56)
        );

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("group_consumer_test", "false", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        ConsumerFactory<String, LogMessageDTO> cf = new DefaultKafkaConsumerFactory<>(
                consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(LogMessageDTO.class, false)
        );
        Consumer<String, LogMessageDTO> consumerServiceTest = cf.createConsumer();
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumerServiceTest, TEST_TOPIC);

        System.out.println("Отправка сообщения в Кафку: " + testLogMessageDTO);
        kafkaLogProducerService.sendLogMessage(testLogMessageDTO);
        Thread.sleep(1000);

        System.out.println("Сообщение отправлено в Кафку");

        ConsumerRecord<String, LogMessageDTO> consumerRecordOfLogMessageDTO =
                KafkaTestUtils.getSingleRecord(consumerServiceTest, TEST_TOPIC);

        LogMessageDTO valueReceived = consumerRecordOfLogMessageDTO.value();

        assertEquals("test-topic", valueReceived.getServiceName());
        assertEquals(LogLevel.ERROR, valueReceived.getLogLevel());
        assertEquals("test-message", valueReceived.getMessage());

        consumerServiceTest.close();

    }
}
