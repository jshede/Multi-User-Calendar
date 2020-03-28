package com.server.backend;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GroupMembership {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = true)
	private Role role;

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group g) {
		this.group = g;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User u) {
		this.user = u;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role r) {
		this.role = r;
	}

	public Integer getId() {
		return this.id;
	}
}
