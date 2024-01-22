package com.kafkaproject.eventsconsumer.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingEvent {
    @Id
    Integer shoppingEventId;

    @Enumerated(EnumType.STRING)
    ShoppingEventType shoppingEventType;

    @OneToOne(mappedBy = "shoppingEvent", cascade = {CascadeType.ALL})
    Item item;
}
