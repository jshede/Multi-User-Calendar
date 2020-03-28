package com.server.backend;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class EventComment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private String comment;

	private Timestamp time;

	private boolean isPrivate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "self_id")
	private EventComment parent;

	public Integer getId() {
		return this.id;
	}

	public Event getEvent() {
		return this.event;
	}

	public void setEvent(Event e) {
		this.event = e;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User u) {
		this.user = u;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String c) {
		this.comment = c;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp t) {
		this.time = t;
	}

	public boolean isPrivate() {
		return this.isPrivate;
	}

	public void setPrivate(boolean p) {
		this.isPrivate = p;
	}

	public EventComment getParent() {
		return this.parent;
	}

	public void setParent(EventComment e) {
		this.parent = e;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
