package com.server.backend.Controllers;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

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

import com.server.backend.Event;
import com.server.backend.EventComment;
import com.server.backend.EventCommentRepository;
import com.server.backend.EventRepository;
import com.server.backend.Group;
import com.server.backend.GroupMembership;
import com.server.backend.GroupMembershipRepository;
import com.server.backend.GroupRepository;
import com.server.backend.HeatMapDayRepository;
import com.server.backend.User;
import com.server.backend.UserRepository;
import com.server.backend.services.CommentMessageHandler;
import com.server.backend.services.RoleService;
import com.server.backend.staticHelpers.HeatMapHelper;
import com.server.backend.staticHelpers.userHelper;

import FrontEndObjects.FrontEndEvent;
import FrontEndObjects.FrontEndEventComment;

/**
 * Handles Event related requests
 *
 * @author jkcowen, bhendel
 *
 */
@Controller
public class EventController {

	@Autowired
	EventRepository eventRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	GroupMembershipRepository groupMemRepos;

	@Autowired
	EventCommentRepository eventComments;

	@Autowired
	HeatMapDayRepository hmRepo;

	@Autowired
	RoleService roles;

	/**
	 * Adds specified event. Id is unnecessary.
	 *
	 * @param session No need to provide.
	 * @param fe      The frontend wrapper for the event requesting to be created.
	 * @return HTTP 200 if successful, HTTP 500 otherwise
	 */
	@PostMapping("/api/addEvent")
	public ResponseEntity addEvent(HttpSession session, @RequestBody FrontEndEvent fe) {
		Event e = new Event();
		User u = null;
		try {
			u = (User) session.getAttribute("user");
			if (u == null)
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			if (!roles.isAtLeastModerator(u.getId(), fe.groupId))
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		e.setUser(u);

		e.setDescription(fe.description);

		try {
			e.setEnd(Timestamp.valueOf(fe.end));
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try {
			e.setStart(Timestamp.valueOf(fe.start));
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Group g = null;
		try {
			g = groupRepository.findById(fe.groupId).get();
		} catch (NoSuchElementException ex) {
			ex.printStackTrace();
		}
		e.setGroup(g);

		e.setHighPriority(fe.isHighPriority);

		e.setLocation(fe.location);

		e.setName(fe.name);

		eventRepository.save(e);

		HeatMapHelper.addEventHM(groupMemRepos, e, hmRepo);

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Returns all events in the requested group. Returns empty list of user is not
	 * part of group.
	 *
	 * @param session No need to provide.
	 * @param groupId This is the id of the group you want to get events from.
	 * @return Events in the requested group.
	 */
	@GetMapping("/api/getEvents")
	public @ResponseBody List<FrontEndEvent> getEvents(HttpSession session, @RequestParam int groupId) {
		boolean userIsAuthentic = false;
		for (GroupMembership gm : groupMemRepos.findAll()) {
			if (gm.getGroup().getId().intValue() == groupId
					&& gm.getUser().getId().intValue() == ((User) session.getAttribute("user")).getId().intValue()) {
				userIsAuthentic = true;
				break;
			}
		}
		Iterable<Event> events = eventRepository.findAll();
		ArrayList<FrontEndEvent> userEvents = new ArrayList<>();
		if (userIsAuthentic) {
			for (Event e : events) {
				if (e.getGroup().getId().intValue() == groupId) {
					userEvents.add(new FrontEndEvent(e));
				}
			}
		}
		return userEvents;

	}

	/**
	 * Delete an event by its ID
	 *
	 * @param session Session
	 * @param eventId Event ID
	 * @return HTTP 200 if successful, HTTP 500 otherwise
	 */
	@PostMapping("/api/deleteEvent")
	public ResponseEntity deleteEvent(HttpSession session, @RequestParam int eventId) {

		if (!(roles.isAtLeastModerator(userHelper.sessionToUserId(session),
				eventRepository.findById(eventId).get().getGroup().getId()))) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}

		Event e;
		try {
			e = eventRepository.findById(new Integer(eventId)).get();
			eventRepository.deleteById(eventId);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		HeatMapHelper.deleteEventHM(groupMemRepos, e, hmRepo);

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Update an event that already exists
	 *
	 * @param session Session
	 * @param event   Front end event with updates made
	 * @return HTTP 200 if successful, HTTP 500 otherwise
	 */
	@PostMapping("/api/updateEvent")
	public ResponseEntity updateEvent(HttpSession session, @RequestParam FrontEndEvent event) {

		if (!(roles.isAdministrator(userHelper.sessionToUserId(session), event.id))) {
			return new ResponseEntity(HttpStatus.FORBIDDEN);
		}
		Event e;
		Event eLast;
		try {

			e = eventRepository.findById(event.id).get();
			eLast = new Event();
			eLast.setId(e.getId());
			eLast.setDescription(e.getDescription());
			e.setDescription(event.description);
			eLast.setEnd(e.getEnd());
			e.setEnd(new Timestamp(Timestamp.parse(event.end)));
			eLast.setStart(e.getStart());
			e.setStart(new Timestamp(Timestamp.parse(event.start)));
			eLast.setGroup(e.getGroup());
			e.setGroup(groupRepository.findById(event.groupId).get());
			eLast.setHighPriority(e.highPriority());
			e.setHighPriority(event.isHighPriority);
			eLast.setLocation(e.getLocation());
			e.setLocation(event.location);
			eLast.setName(e.getName());
			e.setName(event.name);
			eLast.setUser(e.getUser());
			e.setUser((User) session.getAttribute("user"));

			this.eventRepository.save(e);

		} catch (Exception ex) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		HeatMapHelper.updateEventHM(groupMemRepos, e, eLast, hmRepo);

		return new ResponseEntity(HttpStatus.OK);
	}

	/**
	 * Returns a list of events for a given user id
	 *
	 * @param session Session
	 * @param userId  ID of user to get events for
	 * @return List of events for user with given ID
	 */
	@GetMapping("/api/getEventsForUser")
	public @ResponseBody List<FrontEndEvent> getEventsForUser(HttpSession session, @RequestParam int userId) {

		if (userHelper.sessionToUserId(session) != userId) {
			return new ArrayList<>();
		}

		List<FrontEndEvent> ret = new ArrayList<>();

		List<Group> memberOf = new ArrayList<>();

		Iterable<GroupMembership> memberships = this.groupMemRepos.findAll();
		for (GroupMembership g : memberships) {
			if (g.getUser().getId() == userId) {
				memberOf.add(g.getGroup());
			}
		}

		Iterable<Event> events = this.eventRepository.findAll();
		for (Event e : events) {
			if (memberOf.contains(e.getGroup())) {
				ret.add(new FrontEndEvent(e));
			}
		}

		return ret;
	}

	/**
	 * Gets comments for an event
	 *
	 * @param session Session
	 * @param eventId ID of event to get comments for
	 * @return List of comments for event. No particular ordering.
	 */
	@GetMapping("/api/getEventFeedback")
	public @ResponseBody List<FrontEndEventComment> getEventFeedback(HttpSession session, @RequestParam int eventId) {
		// TODO: check user permissions with role controller here

		List<FrontEndEventComment> ret = new ArrayList<>();

		Iterable<EventComment> comments = this.eventComments.findAll();
		for (EventComment e : comments) {
			if (e.getEvent().getId() == eventId) {
				ret.add(new FrontEndEventComment(e));
			}
		}

		return ret;
	}

	/**
	 * Post a comment about an event
	 *
	 * @param session   Session
	 * @param eventId   ID of event to comment on
	 * @param parentId  Parent of comment (use to create sub-comments)
	 * @param comment   Text of comment
	 * @param isPrivate Specifies if this comment is private
	 * @return HTTP 200 if successful, HTTP 500 otherwise
	 */
	@GetMapping("/api/postEventFeedback")
	public ResponseEntity postEventFeedback(HttpSession session, @RequestParam Integer eventId,
			@RequestParam(required = false, defaultValue = "-1") int parentId, @RequestParam String comment,
			@RequestParam boolean isPrivate) {
		// TODO: check user permissions with role controller here

		try {

			EventComment toAdd = new EventComment();

			toAdd.setComment(comment);
			toAdd.setEvent(this.eventRepository.findById(eventId).get());
			toAdd.setTime(new Timestamp((new java.util.Date()).getTime()));
			toAdd.setPrivate(isPrivate);
			toAdd.setUser(userHelper.sessionToUser(session));
			
			Random rand = new Random();
			toAdd.setId(rand.nextInt(2147483647) + 1); 
			
			if (parentId != -1) {
				toAdd.setParent(this.eventComments.findById(parentId).get());
			}

			// post new comment to real time messaging service
			// TODO: move this to after add
			CommentMessageHandler.getInstance().sendComment(toAdd);

			this.eventComments.save(toAdd);

		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity(HttpStatus.OK);
	}
}
