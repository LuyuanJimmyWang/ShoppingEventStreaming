package com.kafkaproject.eventsproducer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafkaproject.eventsproducer.dto.Item;
import com.kafkaproject.eventsproducer.dto.ShoppingEvent;
import com.kafkaproject.eventsproducer.dto.ShoppingEventType;

public class TestUtil {

    public static Item itemRecord(){
        return new Item(123, "a","Kafka Using Spring Boot" );
    }

    public static Item itemRecordWithInvalidValues(){

        return new Item(null, "","Kafka Using Spring Boot" );
    }

    public static ShoppingEvent shoppingEventRecord(){
        return
                new ShoppingEvent(null,
                        ShoppingEventType.NEW,
                        itemRecord());
    }

    public static ShoppingEvent newShoppingEventRecordWithShoppingEventId(){
        return
                new ShoppingEvent(123,
                        ShoppingEventType.NEW,
                        itemRecord());
    }

    public static ShoppingEvent shoppingEventRecordUpdate(){
        return
                new ShoppingEvent(123,
                        ShoppingEventType.UPDATE,
                        itemRecord());
    }

    public static ShoppingEvent shoppingEventRecordUpdateWithNullShoppingEventId(){
        return
                new ShoppingEvent(null,
                        ShoppingEventType.UPDATE,
                        itemRecord());
    }

    public static ShoppingEvent shoppingEventRecordWithInvalidItem(){
        return
                new ShoppingEvent(null,
                        ShoppingEventType.NEW,
                        itemRecordWithInvalidValues());
    }

    public static ShoppingEvent parseShoppingEventRecord(ObjectMapper objectMapper, String json){
        try {
            return objectMapper.readValue(json, ShoppingEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}