package com.kafkaproject.eventsconsumer.dto;

import jakarta.validation.constraints.*;

// 改成数据库的data model

public record Item(@NotNull Integer itemId, @NotBlank String itemName, @NotBlank String itemDescription) {

}
