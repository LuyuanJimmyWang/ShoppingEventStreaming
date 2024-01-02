package com.kafkaproject.eventsproducer.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaproject.eventsproducer.dto.ShoppingEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.concurrent.CompletableFuture;
import java.util.function.ToDoubleBiFunction;

import static com.kafkaproject.eventsproducer.TestUtil.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ShoppingEventsProducerUnitTest {
    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    ShoppingEventsProducer shoppingEventsProducer;

    @Spy
    ObjectMapper objectMapper;

    @Mock
    KafkaTemplate<Integer, String> kafkaTemplate;

    @Test
    void sendShoppingEvent_HappyPath() throws Exception {
        // 1. mock function postLibraryEvent
        ShoppingEvent shoppingEvent = newShoppingEventRecordWithShoppingEventId();

        String record = objectMapper.writeValueAsString(shoppingEvent);

        // CompletableFuture<SendResult<Integer, String>> completableFuture = new CompletableFuture<SendResult<Integer, String>>();
        ProducerRecord<Integer, String> producerRecord = new ProducerRecord("shopping-events", shoppingEvent.eventId(), record);
        TopicPartition topicPartition = new TopicPartition("shopping-events", 1);
        RecordMetadata recordMetadata = new RecordMetadata(topicPartition,1, 1, 1, 1, 1);
        SendResult<Integer, String> sendResult = new SendResult(producerRecord, recordMetadata);
        var future = CompletableFuture.supplyAsync(() -> sendResult);
        when(kafkaTemplate.send(null, shoppingEvent.eventId(), record)).thenReturn(future);
        var completableFuture = shoppingEventsProducer.sendShoppingEvent(newShoppingEventRecordWithShoppingEventId());

        SendResult<Integer, String> actualSendResult = completableFuture.get();
        assert actualSendResult.getRecordMetadata().partition() == 1;
    }

    @Test
    void sendShoppingEvent_BadPath() throws Exception {
        ShoppingEvent shoppingEvent = shoppingEventRecordUpdateWithNullShoppingEventId();

        String record = objectMapper.writeValueAsString(shoppingEvent);

        CompletableFuture<SendResult<Integer, String>> completableFuture = new CompletableFuture<>();
        completableFuture.completeExceptionally(new RuntimeException("Error sending the message"));

        when(kafkaTemplate.send(null, shoppingEvent.eventId(), record)).thenReturn(completableFuture);

        var actualCompletableFuture = shoppingEventsProducer.sendShoppingEvent(shoppingEvent);

        assertThrows(Exception.class, () -> actualCompletableFuture.get(), "Error sending the message");
    }

}











