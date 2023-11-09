package com.kafkaproject.eventsproducer.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaproject.eventsproducer.dto.ShoppingEvent;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class ShoppingEventsProducer {
    /*
    Kafka Template initialization:
    Send Event function:
    call topic name:
     */
    @Value("${spring.kafka.topic}")
    public String topic;

    private final KafkaTemplate<Integer, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    public ShoppingEventsProducer(KafkaTemplate<Integer, String> template, ObjectMapper mapper){
        kafkaTemplate = template;
        objectMapper = mapper;
    }

    public CompletableFuture<SendResult<Integer, String>> sendShoppingEvent(ShoppingEvent shoppingEvent) throws JsonProcessingException {
        var key = shoppingEvent.eventId();
        var value = objectMapper.writeValueAsString(shoppingEvent);
        // async call
        // 1: get metadata of kafka cluster
        // 2: send message and return the future
        var completableFuture = kafkaTemplate.send(topic, key, value);
        return completableFuture
                .whenComplete((sendResult, throwable) -> {
                    if(throwable!=null){
                        handleFailure(key, value, throwable);
                    }else{
                        handleSuccess(key, value, sendResult);
                    }
                });
    }

    private void handleFailure(Integer key, String value, Throwable ex){
        log.error("Error sending the message and the exception is {} ", ex.getMessage(), ex );
    }

    private void handleSuccess(Integer key, String value, SendResult<Integer, String> sendResult){
        log.info("Message Sent Successfully for the key : {} and the value : {} , partition is {} ",
                key, value, sendResult.getRecordMetadata().partition());
    }

}
