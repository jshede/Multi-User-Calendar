package com.server.backend;

import org.springframework.data.repository.CrudRepository;

import com.server.backend.GroupMembership;

public interface GroupMembershipRepository extends CrudRepository<GroupMembership, Integer> {

}