package FrontEndObjects;

import java.io.Serializable;

import com.server.backend.EventComment;

public class FrontEndEventComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String comment;
	public int eventId;
	public int id;
	public boolean isPrivate;
	public int parentId;
	public String time;
	public int userId;

	public FrontEndEventComment() {
	}

	public FrontEndEventComment(EventComment e) {
		try {
			this.comment = e.getComment();
		} catch (NullPointerException p) {
			this.comment = "";
		}

		try {
			this.id = e.getId();
		} catch (NullPointerException p) {
			this.id = -1;
		}

		try {
			this.eventId = e.getEvent().getId();
		} catch (NullPointerException p) {
			this.eventId = -1;
		}

		try {
			this.isPrivate = e.isPrivate();
		} catch (NullPointerException p) {
			this.isPrivate = false;
		}

		try {
			this.parentId = e.getParent().getId();
		} catch (NullPointerException p) {
			this.parentId = -1;
		}

		try {
			this.time = e.getTime().toString();
		} catch (NullPointerException p) {
			this.time = null;
		}

		try {
			this.userId = e.getUser().getId();
		} catch (NullPointerException p) {
			this.userId = -1;
		}
	}

}
