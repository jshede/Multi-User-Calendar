package FrontEndObjects;

import java.io.Serializable;

import com.server.backend.GroupInvitation;

public class FrontEndGroupInvitation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int groupId;
	public int id;
	public int receiverId;
	public int senderId;

	public FrontEndGroupInvitation() {
	}

	public FrontEndGroupInvitation(GroupInvitation gi) {
		try {
			this.groupId = gi.getGroup().getId();
		} catch (NullPointerException n) {
			this.groupId = -1;
		}

		try {
			this.id = gi.getId();
		} catch (NullPointerException n) {
			this.id = -1;
		}

		try {
			this.receiverId = gi.getReceiver().getId();
		} catch (NullPointerException n) {
			this.receiverId = -1;
		}

		try {
			this.senderId = gi.getSender().getId();
		} catch (NullPointerException n) {
			this.senderId = -1;
		}
	}

}
