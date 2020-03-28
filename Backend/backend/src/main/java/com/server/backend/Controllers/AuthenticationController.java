package com.server.backend.Controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.server.backend.User;
import com.server.backend.UserRepository;
import com.server.backend.staticHelpers.userHelper;

import FrontEndObjects.LoginData;

/**
 * Handles authentication of users and session management
 *
 * @author bhendel
 *
 */

@Controller
public class AuthenticationController {

	@Autowired
	private UserRepository users;


	/**
	 * Logs in users
	 *
	 * @param login User credentials in the form of a LoginData object
	 * @param session Session
	 * @return 200 OK of credentials valid, 403 Forbidden otherwise
	 */
	@PostMapping("/api/login")
	@ResponseBody
	public ResponseEntity login(@RequestBody LoginData login, HttpSession session) {
		User user = null;
		Iterable<User> userList = users.findAll();
		for (User u : userList) {
			// Ignore shadow users
			if (u == null || u.getUsername() == null || u.getPasswordHash() == null) {
				continue;
			}

			if (u.getUsername().equals(login.username) && u.getPasswordHash().equals(login.password)) {
				user = u;
				break;
			}
		}

		if (user != null)
			session.setAttribute("user", user);
		else
			return new ResponseEntity(HttpStatus.FORBIDDEN);

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Logs out users, terminates current session
	 *
	 * @param request Request object
	 * @return 200 OK
	 */
	@GetMapping("/api/logout")
	@ResponseBody
	public ResponseEntity logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Registers new users
	 *
	 * @param newUser Information for creating new user
	 * @param session Session
	 * @return 200 OK if successful, 409 conflict if anything goes wrong (i.e. username already exists)
	 */
	@PostMapping("/api/register")
	@ResponseBody
	public ResponseEntity register(@RequestBody User newUser, HttpSession session) {
		this.users.save(newUser);

		try {
			session.setAttribute("user", newUser);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.CONFLICT);
		}

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Returns user object for current user
	 *
	 * @param session Session
	 * @return User object for current user containing name, username, email, and password
	 */
	@GetMapping("/api/getCurrentUser")
	public @ResponseBody User getCurrentUser(HttpSession session) {
		return (User) (session.getAttribute("user"));
	}

    /**
     * Returns id of the current UserRepository
     *
     * @param session Session
     * @return id of the current user
     */
     @GetMapping("/api/getUserId")
     public @ResponseBody int getCurrentId(HttpSession session) {
         int id = userHelper.sessionToUserId(session);
         return  id;
     }

}
