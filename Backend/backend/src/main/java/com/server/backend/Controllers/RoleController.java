package com.server.backend.Controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.server.backend.services.RoleService;
import com.server.backend.staticHelpers.userHelper;

public class RoleController {
	
	@Autowired
	RoleService roles;

	/**
	 * Make a user a moderator of a group.
	 * 
	 * @param session session
	 * @param userId  user id to grant privileges to
	 * @param groupId group id group to grant privileges in
	 * @return 200 OK if authorized, 403 forbidden otherwise
	 */
	@PostMapping("/api/makeModerator")
	public ResponseEntity grantModerator(HttpSession session, int userId, int groupId) {
		try {
			if (roles.isAtLeastModerator(userHelper.sessionToUserId(session), groupId)) {
				roles.setModerator(userId, groupId);
				return new ResponseEntity(HttpStatus.OK);
			}
		} catch (NullPointerException e) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		return new ResponseEntity(HttpStatus.FORBIDDEN);
	}

	/**
	 * Take away moderator privileges.
	 * 
	 * @param session session
	 * @param userId  user id to revoke privileges from
	 * @param groupId group id to revoke privileges in
	 * @return 200 OK if authorized, 403 forbidden otherwise
	 */
	@PostMapping("/api/revokeModerator")
	public ResponseEntity revokeModerator(HttpSession session, int userId, int groupId) {
		try {
			if (roles.isAtLeastModerator(userHelper.sessionToUserId(session), groupId)) {
				roles.revokeModerator(userId, groupId);
				return new ResponseEntity(HttpStatus.OK);
			}
		} catch (NullPointerException e) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity(HttpStatus.FORBIDDEN);
	}

	/**
	 * Kick a user out of a group.
	 * 
	 * @param session session
	 * @param userId  ID of user to kick out
	 * @param groupId group to kick out of
	 * @return 200 OK if authorized, 403 forbidden otherwise
	 */
	@PostMapping("/api/kickUser")
	public ResponseEntity kickUser(HttpSession session, int userId, int groupId) {
		try {
			if (roles.isAtLeastModerator(userHelper.sessionToUserId(session), groupId)) {
				roles.kickUser(userId, groupId);
				return new ResponseEntity(HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity(HttpStatus.OK);
	}

}
