package com.server.backend.staticHelpers;

import javax.servlet.http.HttpSession;

import com.server.backend.User;

public class userHelper {
	
	/**
	 * Gets the user id of a session.
	 * @param session
	 * @return userId for session
	 * @throws NullPointerException
	 */
	public static int sessionToUserId(HttpSession session) throws NullPointerException
	{
		return sessionToUser(session).getId();
	}
	
	/**
	 * Gets the user object of a session.
	 * @param session
	 * @return
	 */
	public static User sessionToUser(HttpSession session)
	{
		return (User) (session.getAttribute("user"));
	}

}
