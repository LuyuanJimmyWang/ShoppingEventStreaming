package com.kafkaproject.eventsconsumer.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    int itemId;

    String itemName;
    String itemDescription;

    @OneToOne
    @JoinColumn(name = "shoppingEventId")
    ShoppingEvent shoppingEvent;
}
