package knowledgehunters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class Option {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private Question question;
	
	private String description;
	@Column(name = "is_corrrect", nullable = false)
	@Type(type = "org.hibernate.type.NumericBooleanType")
	private boolean isCorrect;
	private int position;
	
	@ManyToOne
	private Option comboOption;
	
	public Option() {
		
	}

	public Option(int id, Question question, String description, boolean isCorrect, int position, Option comboOption) {
		this.id = id;
		this.question = question;
		this.description = description;
		this.isCorrect = isCorrect;
		this.position = position;
		this.comboOption = comboOption;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Option getComboOption() {
		return comboOption;
	}

	public void setComboOption(Option comboOption) {
		this.comboOption = comboOption;
	}
}
