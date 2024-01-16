package com.kafkaproject.eventsconsumer.repository;

import com.kafkaproject.eventsconsumer.dto.ShoppingEvent;
import org.springframework.data.repository.CrudRepository;

public interface ShoppingEventsRepository extends CrudRepository<ShoppingEvent,Integer> {}