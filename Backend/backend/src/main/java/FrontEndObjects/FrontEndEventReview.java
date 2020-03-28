package FrontEndObjects;

import java.io.Serializable;

import com.server.backend.EventReview;

public class FrontEndEventReview implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public int eventId;
	public int userId;
	public String start;
	public String end;
	public boolean isAccepted;

	public FrontEndEventReview() {
	}

	public FrontEndEventReview(EventReview er) {
		try {
			this.id = er.getId();
		} catch (NullPointerException n) {
			this.id = -1;
		}
		try {
			this.eventId = er.getEvent().getId();
		} catch (NullPointerException n) {
			this.eventId = -1;
		}

		try {
			this.userId = er.getUser().getId();
		} catch (NullPointerException n) {
			this.userId = -1;
		}

		try {
			this.start = er.getStart().toString();
		} catch (NullPointerException n) {
			this.start = null;
		}

		try {
			this.end = er.getEnd().toString();
		} catch (NullPointerException n) {
			this.end = null;
		}

		try {
			this.isAccepted = er.isAccepted();
		} catch (NullPointerException n) {
			this.isAccepted = false;
		}
	}

}
