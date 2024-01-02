package com.kafkaproject.eventsproducer.producer;

import com.kafkaproject.eventsproducer.TestUtil;
import com.kafkaproject.eventsproducer.dto.ShoppingEvent;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.SendResult;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaproject.eventsproducer.dto.ShoppingEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(topics = "shopping-events")
@TestPropertySource(properties = {
        "spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"
})
class ShoppingEventsProducerIntgTest {

    @Autowired
    private ShoppingEventsProducer shoppingEventsProducer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.topic}")
    private String topic;

    private Consumer<Integer, String> consumer;

    @BeforeEach
    void setUp() {
        var configs = new HashMap<>(KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker));
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        consumer = new DefaultKafkaConsumerFactory<>(configs, new IntegerDeserializer(), new StringDeserializer()).createConsumer();
        embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
    }

    @AfterEach
    void tearDown() {
        consumer.close();
    }

    @Test
    void postSendShoppingEvent() throws Exception {
        // Given
        ShoppingEvent shoppingEvent = TestUtil.newShoppingEventRecordWithShoppingEventId();

        // When
        SendResult<Integer, String> sendResult = shoppingEventsProducer.sendShoppingEvent(shoppingEvent).get();

        // Then
        assertEquals("shopping-events", sendResult.getRecordMetadata().topic());

        // Assertion on consumer receivable
        ConsumerRecords<Integer, String> consumerRecords = KafkaTestUtils.getRecords(consumer);
        assertEquals(1, consumerRecords.count());

        consumerRecords.forEach(record -> {
            var shoppingEventActual = TestUtil.parseShoppingEventRecord(objectMapper, record.value());
            System.out.println("actual event: " + shoppingEventActual);
            assertEquals(shoppingEventActual, TestUtil.newShoppingEventRecordWithShoppingEventId());
        });
    }
}