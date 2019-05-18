package knowledgehunters.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import knowledgehunters.enums.QuestionDifficulty;
import knowledgehunters.enums.QuestionType;

@Entity
public class Question {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String description;
	private QuestionDifficulty difficulty;
	
	@ManyToOne
	private Person author;
	
	private String hint;
	
	@ManyToOne
	private Topic topic;
	
	private QuestionType type;

	public Question() {
		
	}

	public Question(int id, String description, QuestionDifficulty difficulty, Person author, String hint, Topic topic,
			QuestionType type) {
		this.id = id;
		this.description = description;
		this.difficulty = difficulty;
		this.author = author;
		this.hint = hint;
		this.topic = topic;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public QuestionDifficulty getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(QuestionDifficulty difficulty) {
		this.difficulty = difficulty;
	}

	public Person getAuthor() {
		return author;
	}

	public void setAuthor(Person author) {
		this.author = author;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}	
}
