package knowledgehunters.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class GameMove {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	private Game game;
	
	@ManyToOne
	private Student student;
	
	@ManyToOne
	private Question question;
	
	@ManyToOne
	private Option answer;
	
	private String answer_description;
	private int position;
	
	@ManyToOne
	private Question comboAnswer;

	public GameMove() {
		
	}

	public GameMove(int id, Game game, Student student, Question question, Option answer, String answer_description, int position, Question comboAnswer) {
		this.id = id;
		this.game = game;
		this.student = student;
		this.question = question;
		this.answer = answer;
		this.answer_description = answer_description;
		this.position = position;
		this.comboAnswer = comboAnswer;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Option getAnswer() {
		return answer;
	}

	public void setAnswer(Option answer) {
		this.answer = answer;
	}

	public String getAnswer_description() {
		return answer_description;
	}

	public void setAnswer_description(String answer_description) {
		this.answer_description = answer_description;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Question getComboAnswer() {
		return comboAnswer;
	}

	public void setComboAnswer(Question comboAnswer) {
		this.comboAnswer = comboAnswer;
	}
}
