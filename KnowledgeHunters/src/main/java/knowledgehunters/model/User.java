package knowledgehunters.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import knowledgehunters.model.Role;

@Entity
@Table(name="users")
public class User {
	@Id
	private String id;
	private String username;
	private String password;
	@ManyToOne
	private Role role;
	
	public User() {
		
	}
	
	public User(String id, String username, String password, Role role) {
//		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
}
