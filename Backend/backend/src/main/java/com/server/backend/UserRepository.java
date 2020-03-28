package com.server.backend;

import org.springframework.data.repository.CrudRepository;

import com.server.backend.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}