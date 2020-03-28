package com.server.backend.services;

import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import com.server.backend.EventComment;
import com.server.backend.EventCommentRepository;
import com.server.backend.GroupMembership;
import com.server.backend.GroupMembershipRepository;
import com.server.backend.GroupRepository;
import com.server.backend.Role;
import com.server.backend.RoleRepository;
import com.server.backend.User;
import com.server.backend.UserRepository;

@Service
public final class RoleService {
	
	@Autowired
	RoleRepository rolesRepository;
	
	@Autowired
	GroupMembershipRepository groupMembershipRepository;
	
	@Autowired
	GroupRepository groupRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	EventCommentRepository eventCommentRepo;

	/**
	 * Makes a user the administrator of a group.
	 * 
	 * @param userId  ID of user
	 * @param groupId ID of group
	 */
	public void setAdministrator(int userId, int groupId) {
		createAndSet(userId, groupId, 'A');
	}

	/**
	 * Makes user a moderator of a group.
	 * 
	 * @param userId  ID of user
	 * @param groupId ID of group
	 */
	public void setModerator(int userId, int groupId) {
		createAndSet(userId, groupId, 'M');
	}

	/**
	 * Makes a user an unprivileged member of a group.
	 * 
	 * @param userId  ID of user
	 * @param groupId ID of group
	 */
	public void makeUser(int userId, int groupId) {
		createAndSet(userId, groupId, 'U');
	}

	/**
	 * Returns true if a user is allowed to see the given comment.
	 * 
	 * @param userId    ID of user wanting to see comment
	 * @param commentId ID of the comment
	 * @return
	 */
	public boolean canSeeComment(int userId, int commentId) {
		
		EventComment c = eventCommentRepo.findById(commentId).get();
		
		int groupId = c.getEvent().getGroup().getId();
		
		Iterable<GroupMembership> memberships = groupMembershipRepository.findAll();
		for(GroupMembership g : memberships)
		{
			if(g.getUser().getId() == userId && g.getGroup().getId() == groupId)
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Returns true if a user is a member of a given group
	 * 
	 * @param userId  user id
	 * @param groupId group id
	 * @return group membership status
	 */
	public boolean isUser(int userId, int groupId) {
		char privs = getPrivilegesFlag(userId, groupId);
		return privs != '%';
	}

	/**
	 * Returns true of a user is a moderator or admin in a given group
	 * 
	 * @param userId  user id
	 * @param groupId group id
	 * @return group moderator status
	 */
	public boolean isAtLeastModerator(int userId, int groupId) {
		char privs = getPrivilegesFlag(userId, groupId);
		return privs == 'M' || isAdministrator(userId, groupId);
	}

	/**
	 * Returns true if a user is an administrator of a group.
	 * 
	 * @param userId  user id
	 * @param groupId group id
	 * @return user admin status
	 */
	public boolean isAdministrator(int userId, int groupId) {
		char privs = getPrivilegesFlag(userId, groupId);
		return privs == 'A';
	}

	/**
	 * If a role for the given user in the given group does not exist, then create
	 * it. Set the privilege char flag for the user's role entry.
	 * 
	 * @param userId     ID of user
	 * @param groupId    ID of group
	 * @param privileges privileges glag
	 */
	private void createAndSet(int userId, int groupId, char privileges) {
		
		Iterable<GroupMembership> memberships = groupMembershipRepository.findAll();
		ArrayList<GroupMembership> userMemberships = new ArrayList();
		
		for(GroupMembership g : memberships)
		{
			if(g.getUser().getId() == userId && g.getGroup().getId() == groupId)
			{
				userMemberships.add(g);
			}
		}
		
		for(GroupMembership m : userMemberships) // there should only be one m in the list
		{
			Role r = m.getRole();
			
			if(r == null)
			{
				r = new Role();
				
				r.setGroup(groupRepository.findById(groupId).get());
				r.setName(userRepository.findById(userId).get().getName());
				r.setPermissions(privileges);
				r.setId(6); // It doesn't make sense to me either
			}
			
			// save role
			r.setPermissions(privileges);
			rolesRepository.save(r);
			
			// add to membership
			Role correctRole = null;
			Iterable<Role> roles = rolesRepository.findAll();
			for(Role cur : roles)
			{
				if(cur.getGroup().getId() == groupId & cur.getName().equals(m.getUser().getName()))
				{
					correctRole = cur;
					break;
				}
			}
			m.setRole(correctRole);
			groupMembershipRepository.save(m);
		}
		
		if(userMemberships.size() == 0) 
		{
			Role r = new Role();
			
			r.setGroup(groupRepository.findById(groupId).get());
			r.setName(userRepository.findById(userId).get().getName());
			r.setPermissions(privileges);
			
			Random rand = new Random();
			r.setId(rand.nextInt(2147483647) + 1);
			
			rolesRepository.save(r);
		}
	}

	/**
	 * Looks in the role table and returns privilege flag for user in a group. If no
	 * entry in the role table is found, '%' is returned.
	 * 
	 * @param userId  user id
	 * @param groupId group id
	 * @return User privilege flag
	 */
	private char getPrivilegesFlag(int userId, int groupId) {
		
		Iterable<GroupMembership> memberships = groupMembershipRepository.findAll();
		ArrayList<GroupMembership> userMemberships = new ArrayList();
		
		for(GroupMembership g : memberships)
		{
			if(g.getUser().getId() == userId && g.getGroup().getId() == groupId)
			{
				userMemberships.add(g);
			}
		}
		
		for(GroupMembership m : userMemberships) // there should only be one m in the list
		{
			Role r = m.getRole();
			
			if(r == null)
			{
				return '%';
			}
			
			return r.getPermissions();
		}
		
		return '%';
	}
	
	/**
	 * Revokes moderator provilages in group
	 * @param userId
	 * @param groupId
	 */
	public void revokeModerator(int userId, int groupId)
	{
		Iterable<Role> roleList = rolesRepository.findAll();
		User u = userRepository.findById(userId).get();
		for(Role r : roleList)
		{
			if(r.getName().equals(u.getName()) && r.getGroup().getId() == groupId)
			{
				r.setPermissions('U');
				rolesRepository.save(r);
			}
		}
	}
	
	/**
	 * Kick user from group
	 * @param userId
	 * @param groupId
	 */
	public void kickUser(int userId, int groupId)
	{
		Iterable<GroupMembership> memberships = groupMembershipRepository.findAll();
		Iterable<Role> roleList = rolesRepository.findAll();
		
		User u = userRepository.findById(userId).get();
		
		for(GroupMembership g : memberships)
		{
			if(g.getUser().getId() == userId && g.getGroup().getId() == groupId)
			{
				groupMembershipRepository.delete(g);
				break;
			}
		}
		
		for(Role r : roleList)
		{
			if(r.getName().equals(u.getName()) && r.getGroup().getId() == groupId)
			{
				rolesRepository.delete(r);
				break;
			}
		}
		
		
	}

}
