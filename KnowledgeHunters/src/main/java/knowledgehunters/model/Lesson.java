package knowledgehunters.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="lesson")
public class Lesson {
	
	@Id
	private String id;
	private String title;
	@ManyToOne
	private Topic topic;
	@ManyToOne
	private Person author;
	
	public Lesson() {
		
	}

	public Lesson(String id, String title, Topic topic, Person author) {
		this.id = id;
		this.title = title;
		this.topic = topic;
		this.author = author;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Person getAuthor() {
		return author;
	}

	public void setAuthor(Person author) {
		this.author = author;
	}
}
