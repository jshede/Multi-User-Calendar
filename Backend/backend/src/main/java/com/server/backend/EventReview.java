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
public class EventReview {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private Timestamp start;

	private Timestamp end;

	private boolean isAccepted;

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

	public Timestamp getStart() {
		return this.start;
	}

	public void setStart(Timestamp s) {
		this.start = s;
	}

	public Timestamp getEnd() {
		return this.end;
	}

	public void setEnd(Timestamp e) {
		this.end = e;
	}

	public boolean isAccepted() {
		return this.isAccepted;
	}

	public void setAccepted(boolean a) {
		this.isAccepted = a;
	}

}
