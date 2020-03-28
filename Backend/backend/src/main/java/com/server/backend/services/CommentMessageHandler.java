package com.server.backend.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

import org.springframework.beans.factory.annotation.Autowired;

import com.server.backend.EventComment;
import com.server.backend.EventCommentRepository;
import com.server.backend.GroupMembership;
import com.server.backend.GroupMembershipRepository;
import com.server.backend.User;

public final class CommentMessageHandler {
	/**
	 * Key is a userId. Value is a queue of eventCommentIds.
	 */
	HashMap<Integer, PriorityQueue<EventComment>> eventCommentQueue = new HashMap<>();

	@Autowired
	GroupMembershipRepository gmRepo;

	@Autowired
	EventCommentRepository commentRepo;

	private static class Holder {
		static CommentMessageHandler instance = new CommentMessageHandler();
	}

	private CommentMessageHandler() {

	}

	public static synchronized CommentMessageHandler getInstance() {
		return Holder.instance;
	}

	public synchronized void closeUserQueue(int userId) {
		eventCommentQueue.remove(new Integer(userId));
	}
	/**
	 * Make a new EventComment available to clients in real time.
	 * 
	 * @param comment New EventComment to be sent to relevant clients in real time
	 */
	public synchronized void sendComment(EventComment comment) {
		for(Integer i : eventCommentQueue.keySet()) {
			PriorityQueue<EventComment> uQueue = eventCommentQueue.get(i);
			if( uQueue == null) {
				uQueue = new PriorityQueue<EventComment>();
				eventCommentQueue.put(i, uQueue);
			}
			uQueue.add(comment);
		}
	}

	/**
	 * Fetch the next new comment for the specified user
	 * 
	 * @param user User to fetch next new comment for
	 * @return Next new comment for user
	 */
	public synchronized EventComment receiveComment(int userId) {
		PriorityQueue<EventComment> uQueue = eventCommentQueue.get(userId);
		if( uQueue == null) {
			uQueue = new PriorityQueue<EventComment>();
			eventCommentQueue.put(userId, uQueue);
		}
		try {
			return eventCommentQueue.get(userId).remove();
		} catch (Exception e) {
			return null;
		}
	}

}
