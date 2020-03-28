package com.server.backend;

import org.springframework.data.repository.CrudRepository;

import com.server.backend.EventComment;

public interface EventCommentRepository extends CrudRepository<EventComment, Integer> {

}