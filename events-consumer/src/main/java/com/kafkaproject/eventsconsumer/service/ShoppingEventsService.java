package com.kafkaproject.eventsconsumer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaproject.eventsconsumer.dto.ShoppingEvent;
import com.kafkaproject.eventsconsumer.repository.ShoppingEventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ShoppingEventsService {

    @Autowired
    private ShoppingEventsRepository shoppingEventsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Process event when received
    public void processLibraryEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException {
        // Step 1: Extract Event From the Consumer Record
        // Step 2: Store event into our database based on the shopping event type
    }

    private void validate(ShoppingEvent event){
        // validate event id
        if(event.getEventId() == null){
            throw new IllegalArgumentException("Event Id cannot be null");
        }

        // validate if UPDATE then exists in database
        Optional<ShoppingEvent> ShoppingEventOptional = shoppingEventsRepository.findById(event.getEventId());
        if(ShoppingEventOptional.isEmpty()){
            throw new IllegalArgumentException("Not a valid shopping event");
        }

        log.info("Validation is successful for the event : {} ", ShoppingEventOptional.get());
    }

    private void save(ShoppingEvent event){
        event.getItem().setShoppingEvent(event);
        shoppingEventsRepository.save(event);
        log.info("Successfully save the event {} ", event);
    }
}