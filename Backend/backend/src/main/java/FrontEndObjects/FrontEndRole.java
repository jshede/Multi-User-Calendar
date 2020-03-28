package FrontEndObjects;

import java.io.Serializable;

import com.server.backend.Role;

public class FrontEndRole implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int groupId;
	public int id;
	public String name;
	public char permissions;

	public FrontEndRole() {
	}

	public FrontEndRole(Role r) {
		try {
			this.groupId = r.getGroup().getId();
		} catch (NullPointerException n) {
			this.groupId = -1;
		}
		try {
			this.id = r.getId();
		} catch (NullPointerException n) {
			this.id = -1;
		}
		try {
			this.name = r.getName();
		} catch (NullPointerException n) {
			this.name = null;
		}
		try {
			this.permissions = r.getPermissions();
		} catch (NullPointerException n) {
			this.permissions = 0;
		}
	}
}
