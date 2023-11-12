package com.kafkaproject.eventsproducer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaproject.eventsproducer.dto.ShoppingEvent;
import com.kafkaproject.eventsproducer.producer.ShoppingEventsProducer;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.kafkaproject.eventsproducer.TestUtil.*;
import static org.mockito.ArgumentMatchers.isA;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(EventController.class)
class EventControllerUnitTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ShoppingEventsProducer shoppingEventsProducer;

    @Test
    void postLibraryEvent_happyPath() throws Exception {
        // 1. provide sample input ShoppingEvent test cases
        var json = objectMapper.writeValueAsString(shoppingEventRecord());
        // 2. mock function postLibraryEvent
        when(shoppingEventsProducer.sendShoppingEvent(isA(ShoppingEvent.class))).thenReturn(null);
        // 3. check output
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/shoppingevent/create")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void postLibraryEvent_4xxError() throws Exception {
        // 1. provide sample input ShoppingEvent test cases
        var json = objectMapper.writeValueAsString(shoppingEventRecordWithInvalidItem());
        // 2. mock function postLibraryEvent
        when(shoppingEventsProducer.sendShoppingEvent(isA(ShoppingEvent.class))).thenReturn(null);
        // 3. check output
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/shoppingevent/create")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}








