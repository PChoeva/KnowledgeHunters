package knowledgehunters.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;

import knowledgehunters.service.GameMoveService;

@Entity
public class Game {
	//game consists of id, overall time(duration), date of game start(timestamp)

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String createdAt;
	private int duration;
	
		
	public Game() {
		
	}

	public Game(int id, String createdAt, int duration) {
		this.id = id;
		this.createdAt = createdAt;
		this.duration = duration;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
