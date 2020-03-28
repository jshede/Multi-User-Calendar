package com.server.backend.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.server.backend.Group;
import com.server.backend.GroupInvitation;
import com.server.backend.GroupInvitationRepository;
import com.server.backend.GroupMembership;
import com.server.backend.GroupMembershipRepository;
import com.server.backend.GroupRepository;
import com.server.backend.User;
import com.server.backend.UserRepository;
import com.server.backend.services.RoleService;
import com.server.backend.staticHelpers.userHelper;

import FrontEndObjects.FrontEndGroup;
import FrontEndObjects.FrontEndUser;

/**
 * Handles group related requests
 *
 * @author jkcowen
 *
 */
@Controller
public class GroupController {

	@Autowired
	GroupMembershipRepository groupMemRepos;

	@Autowired
	GroupInvitationRepository groupInviteRepo;

	@Autowired
	GroupRepository groupRepo;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleService roles;

	/**
	 * Invite a user to a group.
	 *
	 * @param session no need to provide.
	 * @param groupId Id of group to invite user to
	 * @param email   Email of user to invite
	 * @return HTTP 200 if successful, HTTP 500 otherwise
	 */
	@PostMapping("/api/inviteUser")
	public ResponseEntity inviteUser(HttpSession session, @RequestParam int groupId, @RequestParam String email) {
		Object o = session.getAttribute("user");
		if (o == null)
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		User inviter = (User) o;
		if (!roles.isAdministrator(inviter.getId(), groupId))
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		User invitee = null;
		for (User u : userRepo.findAll()) {
			if (u.getEmail().equals(email)) {
				invitee = u;
			}
		}
		if (invitee == null) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		GroupInvitation gi = new GroupInvitation();
		Group group = null;
		try {
			group = groupRepo.findById(groupId).get();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		gi.setGroup(group);

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Respond to an invitation to a group.
	 *
	 * @param session       No need to provide.
	 * @param accept        Accept or decline invite.
	 * @param groupInviteId The groupInvite being responded to.
	 * @return HTTP 200 if successful, HTTP 500 otherwise
	 */
	@PostMapping("/api/inviteResponse")
	public ResponseEntity inviteResponse(HttpSession session, @RequestParam boolean accept,
			@RequestParam int groupInviteId) {
		// TODO Make sure to delete any other invites to same group
		Object o = session.getAttribute("user");
		if (o == null)
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		GroupInvitation gi = groupInviteRepo.findById(groupInviteId).get();
		if (gi == null)
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		User u = gi.getReceiver();
		if (u.getId().intValue() != ((User) o).getId().intValue())
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);

		Group g = gi.getGroup();
		if (u.getId().intValue() != ((User) o).getId().intValue())
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		if (accept & !userInGroup(u.getId(), g.getId())) {
			GroupMembership gm = new GroupMembership();
			gm.setGroup(g);
			gm.setUser(u);
			groupMemRepos.save(gm);
			g.setSize(new Integer(g.getSize().intValue()+1));
			groupRepo.save(g);
		}
		groupInviteRepo.delete(gi);
		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Kick user from a group.
	 *
	 * @param session No need to provide
	 * @param groupId Id of group kicking from
	 * @param userId  Id of user to kick
	 * @return HTTP 200 if successful, HTTP 500 otherwise
	 */
	@PostMapping("/api/#getKicked>:o")
	public ResponseEntity kickUser(HttpSession session, @RequestParam int groupId, @RequestParam int userId) {
		Object o = session.getAttribute("user");
		if (o == null)
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		if (roles.isAdministrator(((User) o).getId(), groupId))
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		if (userInGroup(userId, groupId)) {
			for (GroupMembership gm : groupMemRepos.findAll()) {
				Group g = gm.getGroup();
				if (g.getId().intValue() == groupId && gm.getUser().getId().intValue() == userId) {
					groupMemRepos.delete(gm);
					g.setSize(new Integer(g.getSize().intValue()-1));
					groupRepo.save(g);
					break;
				}
			}
		}
		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Gets the group specified by the requested groupId.
	 *
	 * @param session No need to provide.
	 * @param groupId The id of the group to get info about.
	 * @return The group requested with its info.
	 */
	@GetMapping("/api/getGroupInfo")
	public @ResponseBody FrontEndGroup getGroupInfo(HttpSession session, @RequestParam int groupId) {
		try {
			if (userInGroup(userHelper.sessionToUserId(session), groupId))
				return new FrontEndGroup(groupRepo.findById(groupId).get());
		} catch (NullPointerException e) {
		}
		return null;
	}

	/**
	 * Will update the group in the database with the id specified in group to have
	 * the name and description provided in group
	 *
	 * @param session No need to provide.
	 * @param group   The group to edit.
	 * @return HTTP 200 if successful, HTTP 500 otherwise
	 */
	@PostMapping("/api/editGroup")
	public ResponseEntity editGroup(HttpSession session, @RequestBody FrontEndGroup group) {
		User user = (User) session.getAttribute("user");
		if( user == null ) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		if (roles.isAdministrator(user.getId().intValue(), group.id)) {
			Group g = groupRepo.findById(group.id).get();
			try {
			g.setDescription(group.description);
			g.setName(group.name);
			groupRepo.save(g);
			}catch(NullPointerException e) {
				e.printStackTrace();
				return new ResponseEntity(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity(HttpStatus.OK);
		}
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Creates a new group with the current session user as admin. Do not specify
	 * id.
	 *
	 * @param session No need to provide.
	 * @param group   Group to be created and stored in database.
	 * @return HTTP 200 if successful, HTTP 500 otherwise
	 */
	@PostMapping("/api/createGroup")
	public ResponseEntity createGroup(HttpSession session, @RequestBody FrontEndGroup group) {
		if (session.getAttribute("user") != null) {
			Group g = new Group();
			g.setDescription(group.description);
			g.setName(group.name);
			g.setSize(new Integer(1));
			groupRepo.save(g);
			GroupMembership gm = new GroupMembership();
			gm.setGroup(g);
			gm.setUser((User) session.getAttribute("user"));
			groupMemRepos.save(gm);
			roles.setAdministrator(userHelper.sessionToUserId(session), g.getId());
			return new ResponseEntity(HttpStatus.OK);
		}
		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Returns all groups the current user is a member of. Mapping: api/getGroups
	 *
	 * @param session No need to provide.
	 * @return groups the user is in.
	 */
	@GetMapping("/api/getGroups")
	public @ResponseBody List<FrontEndGroup> getGroups(HttpSession session) {
		User user = (User) session.getAttribute("user");
		ArrayList<FrontEndGroup> groups = new ArrayList<>();

		if(user == null)
			return groups;
		
		for (GroupMembership gm : groupMemRepos.findAll()) {
			if (gm.getUser().getId().equals(user.getId())) {
				groups.add(new FrontEndGroup(gm.getGroup()));
			}
		}

		return groups;
	}

	/**
	 * Delete a group.
	 *
	 * @param session No need to provide
	 * @param groupId ID of group to delete
	 * @return HTTP 200 if successful, HTTP 500 otherwise
	 */
	@PostMapping("/api/deleteGroup")
	public ResponseEntity deleteGroup(HttpSession session, @RequestParam int groupId) {
		// TODO
		Object user = session.getAttribute("user");
		if (user == null)
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		if (roles.isAdministrator(((User) user).getId(), groupId))
			userRepo.deleteById(groupId);
		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Get all the users in a group.
	 *
	 * @param session No need to provide
	 * @param groupId the id of the Group to get users of
	 * @return List of users in specified group
	 */
	@GetMapping("/api/getUsersInGoup")
	public @ResponseBody List<FrontEndUser> getUsersInGroup(HttpSession session, @RequestParam int groupId) {
		// TODO
		Object user = session.getAttribute("user");
		if (user == null)
			return null;
		if (!userInGroup(((User) user).getId(), groupId))
			return null;
		ArrayList<FrontEndUser> users = new ArrayList<>();
		for (GroupMembership gm : groupMemRepos.findAll()) {
			if (gm.getGroup().getId().intValue() == groupId) {
				users.add(new FrontEndUser(gm.getUser()));
			}
		}
		return users;
	}

	/**
	 * Helper method. Determines if a user is in a specified group.
	 * @param userId
	 * @param groupId
	 * @return True if user is in group, false otherwise.
	 */
	public boolean userInGroup(int userId, int groupId) {
		for (GroupMembership gm : groupMemRepos.findAll()) {
			if (gm.getGroup().getId().intValue() == groupId && gm.getUser().getId().intValue() == userId) {
				return true;
			}
		}
		return false;
	}

}
