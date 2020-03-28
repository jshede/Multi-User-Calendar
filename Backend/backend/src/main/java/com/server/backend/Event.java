package com.server.backend;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdByUser_id")
	private User createdBy;

	private String name;

	private String description;

	private Timestamp start;

	private Timestamp end;

	@Column(nullable = true)
	private String location;

	private boolean isHighPriority;

	public Integer getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group g) {
		this.group = g;
	}

	public User getUser() {
		return this.createdBy;
	}

	public void setUser(User u) {
		this.createdBy = u;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String n) {
		this.name = n;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String d) {
		this.description = d;
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

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String l) {
		this.location = l;
	}

	public boolean highPriority() {
		return this.isHighPriority;
	}

	public void setHighPriority(boolean p) {
		this.isHighPriority = p;
	}
}
