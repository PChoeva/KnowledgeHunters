package knowledgehunters.model;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

@Entity
public class Person {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private User user;
	@ManyToOne
	private School school;
	private String displayName;
	private String email;
	@Column(name = "is_approved", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean isApproved=true;
	
	public Person() {
		
	}

	public Person(int id, User user, School school, String displayName, String email, Boolean isApproved) {
		this.id = id;
		this.user = user;
		this.school = school;
		this.displayName = displayName;
		this.email = email;
		this.isApproved = isApproved;
	}
	
	public Person(int id, User user, School school, String displayName, String email) {
		this.id = id;
		this.user = user;
		this.school = school;
		this.displayName = displayName;
		this.email = email;
		this.isApproved = true;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Boolean getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Boolean approved) {
		this.isApproved = approved;
	}

	public Boolean isAdmin() {
		return this.getUser().getRole().getName().equals("Admin") ? true : false;
	}
	
	public Boolean isTeacher() {
		return this.getUser().getRole().getName().equals("Teacher") ? true : false;
	}
	public Boolean isStudent() {
		return this.getUser().getRole().getName().equals("Student") ? true : false;
	}
}
