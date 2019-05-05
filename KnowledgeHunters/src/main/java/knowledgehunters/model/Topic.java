package knowledgehunters.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="topic")
public class Topic {
	
	@Id
	private String id;
	private String name;
	@ManyToOne
	private Subject subject;
	
	public Topic() {
		
	}

	public Topic(String id, String name, Subject subject) {
		this.id = id;
		this.name = name;
		this.subject = subject;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}	
}
