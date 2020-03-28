package FrontEndObjects;

import java.io.Serializable;

import com.server.backend.Group;

public class FrontEndGroup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String name;
	public String description;
	
	public FrontEndGroup()
	{
		
	}
	
	public FrontEndGroup(Group g) {
		id = g.getId().intValue();
		name = g.getName();
		description = g.getDescription();
	}
	
}
