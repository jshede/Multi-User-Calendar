package com.server.backend;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	private String name;

	private char permissions;

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group g) {
		this.group = g;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public char getPermissions() {
		return this.permissions;
	}

	public void setPermissions(char c) {
		this.permissions = c;
	}

	public int getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

}
