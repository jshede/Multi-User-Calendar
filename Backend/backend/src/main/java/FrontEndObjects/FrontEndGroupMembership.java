package FrontEndObjects;

import java.io.Serializable;

import com.server.backend.GroupMembership;

public class FrontEndGroupMembership implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int groupId;
	public int id;
	public int roleId;
	public int userId;

	public FrontEndGroupMembership() {

	}

	public FrontEndGroupMembership(GroupMembership gm) {
		try {
			this.groupId = gm.getGroup().getId();
		} catch (NullPointerException n) {
			this.groupId = -1;
		}
		try {
			this.id = gm.getId();
		} catch (NullPointerException n) {
			this.id = -1;
		}
		try {
			this.roleId = gm.getRole().getId();
		} catch (NullPointerException n) {
			this.roleId = -1;
		}
		try {
			this.userId = gm.getUser().getId();
		} catch (NullPointerException n) {
			this.userId = -1;
		}
	}

}
