package com.kafkaproject.eventsproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kafkaproject.eventsproducer.dto.ShoppingEvent;
import com.kafkaproject.eventsproducer.producer.ShoppingEventsProducer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;



@RestController
@Slf4j
public class EventController {
    private final ShoppingEventsProducer shoppingEventsProducer;

    public EventController(ShoppingEventsProducer shoppingEventsProducer) {
        this.shoppingEventsProducer = shoppingEventsProducer;
    }
    /*
    We gonna create a controller for apis in order to create and update events
    Step: create event and transfer to producer service so that producer can transfer the event to kafka cluster
     */
    @PostMapping("/v1/shoppingevent/create")
    public ResponseEntity<ShoppingEvent> postLibraryEvent(
            @RequestBody @Valid ShoppingEvent shoppingEvent
    ) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
        log.info("shoppingEvent : {} ", shoppingEvent);

        // kafka producer invocation
        shoppingEventsProducer.sendShoppingEvent(shoppingEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(shoppingEvent);
    }
}
