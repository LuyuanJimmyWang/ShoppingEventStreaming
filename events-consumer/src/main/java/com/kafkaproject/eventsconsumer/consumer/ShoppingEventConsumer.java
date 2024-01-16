package com.kafkaproject.eventsconsumer.consumer;

// We will use kafka listener to listen to the msg, receiving from the kafka cluster

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafkaproject.eventsconsumer.service.ShoppingEventsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
public class ShoppingEventConsumer {

    @Autowired
    private ShoppingEventsService shoppingEventsService;

    @KafkaListener(
            topics = {"shopping-events"},
            groupId = "library-events-listener-group")
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        log.info("ConsumerRecord : {} ", consumerRecord);
        shoppingEventsService.processLibraryEvent(consumerRecord);
    }
}