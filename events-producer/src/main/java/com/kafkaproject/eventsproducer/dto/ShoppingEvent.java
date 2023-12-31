package com.kafkaproject.eventsproducer.dto;

import jakarta.validation.Valid;

public record ShoppingEvent(Integer eventId, ShoppingEventType shoppingEventType, @Valid Item item) {
}
