package com.server.backend;

import org.springframework.data.repository.CrudRepository;

import com.server.backend.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {

}
