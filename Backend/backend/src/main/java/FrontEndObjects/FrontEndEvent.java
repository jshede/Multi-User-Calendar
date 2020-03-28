package FrontEndObjects;

import java.io.Serializable;

import com.server.backend.Event;

public class FrontEndEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4025205509987327342L;
	public int creatorId;
	public int groupId;
	public int id;
	public String description;
	public String start;
	public String end;
	public boolean isHighPriority;
	public String location;
	public String name;

	public FrontEndEvent() {
	}

	public FrontEndEvent(Event e) {
		try {
			this.creatorId = e.getUser().getId();
		} catch (NullPointerException p) {
			this.creatorId = -1;
		}
		try {
			this.groupId = e.getGroup().getId();
		} catch (NullPointerException p) {
			this.groupId = -1;
		}

		try {
			this.id = e.getId();
		} catch (NullPointerException p) {
			this.id = -1;
		}

		try {
			this.description = e.getDescription();
		} catch (NullPointerException p) {
			this.description = null;
		}

		try {
			this.start = e.getStart().toString();
		} catch (NullPointerException p) {
			this.start = null;
		}

		try {
			this.end = e.getEnd().toString();
		} catch (NullPointerException p) {
			this.end = null;
		}

		try {
			this.isHighPriority = e.highPriority();
		} catch (NullPointerException p) {
			this.isHighPriority = false;
		}

		try {
			this.location = e.getLocation();
		} catch (NullPointerException p) {
			this.location = null;
		}

		try {
			this.name = e.getName();
		} catch (NullPointerException p) {
			this.name = null;
		}
	}
}
