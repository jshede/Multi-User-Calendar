package FrontEndObjects;

import java.io.Serializable;

import com.server.backend.User;

public class FrontEndUser implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FrontEndUser(User u) {
		this.name = u.getName();
		this.username = u.getUsername();
		this.email = u.getEmail();
	}

	public FrontEndUser() {

	}

	private String name;

	private String username;

	private String email;
	
	private int id;

	public String getName() {
		return this.name;
	}

	public void setName(String n) {
		this.name = n;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String u) {
		this.username = u;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String e) {
		this.email = e;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int i)
	{
		this.id = i;
	}

}