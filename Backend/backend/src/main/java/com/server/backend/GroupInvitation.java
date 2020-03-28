package com.server.backend;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class GroupInvitation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private User sender;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private User receiver;

	public Group getGroup() {
		return this.group;
	}

	public void setGroup(Group g) {
		this.group = g;
	}

	public User getSender() {
		return this.sender;
	}

	public void setSender(User s) {
		this.sender = s;
	}

	public User getReceiver() {
		return this.receiver;
	}

	public void setReceiver(User r) {
		this.receiver = r;
	}

	public int getId() {
		return this.id;
	}

}
