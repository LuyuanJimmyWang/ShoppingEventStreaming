package com.kafkaproject.eventsproducer.dto;

import jakarta.validation.constraints.*;

public record Item(@NotNull Integer itemId, @NotBlank String itemName, @NotBlank String itemDescription) {

}
