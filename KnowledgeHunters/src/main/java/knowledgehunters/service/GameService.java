package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.enums.QuestionDifficulty;
import knowledgehunters.enums.QuestionType;
import knowledgehunters.model.Game;
import knowledgehunters.model.GameMove;
import knowledgehunters.model.Question;
import knowledgehunters.repository.GameRepository;

@Service
public class GameService {
	@Autowired GameRepository gameRepository;
	@Autowired GameMoveService gameMoveService;
	@Autowired OptionService optionService;
	
	public List<Game> getAllGames(){
		List<Game> games = new ArrayList<>();
		gameRepository.findAll().forEach(games ::add);
		return games;
	}
	
	public Optional<Game> getGame(int id){
		
		return  gameRepository.findById(id);
	}

	public void addGame(Game game) {
		
		gameRepository.save(game);
	}
	
	public void updateGame(int id, Game game) {
		
		gameRepository.save(game);
	}
	
	public void deleteGame(int id) {
		
		gameRepository.deleteById(id);
	}
	
	public Game saveGame(Game game) {
	    return gameRepository.save(game);
	}
	
	public int calcGamePoints(Game game, int duration) {
		List<GameMove> gameMoves = gameMoveService.getGameMovesByGameId(game.getId());
		
		int points = 0;
		for (GameMove gameMove : gameMoves) {
			if (gameMove.getQuestion().getType() == QuestionType.OPEN) {
				System.out.println("calcGamePoints answer desc:" + gameMove.getAnswer_description());
				System.out.println("calcGamePoints correct desc:" + gameMove.getQuestion().getDescription());
				if (gameMove.getAnswer_description().toLowerCase().equals(optionService.getAllOptionsByQuestionId(gameMove.getQuestion().getId()).get(0).getDescription().toLowerCase())) {
					points += calcPointsByQuestionDifficulty(gameMove);
				}
			} else {
				if (gameMove.getAnswer().isCorrect()) {
					points += calcPointsByQuestionDifficulty(gameMove);
				}
			}
		}
		
		points *= calcCoeffForPointsByGameDuration(gameMoves.get(0).getQuestion().getDifficulty(), duration);
		return points;
	}
	private double calcPointsByQuestionDifficulty(GameMove gameMove) {
		switch (gameMove.getQuestion().getDifficulty()) {
			case EASY: {return 1;}
			case MEDIUM: {return 2;}
			case HARD: {return 3;}
			default: {return 0;}
		}
	}
	
	private int calcCoeffForPointsByGameDuration(QuestionDifficulty difficulty, int duration) {
		int minDuration;
		switch (difficulty) {
			case EASY: {
				minDuration = 20;
				break;
			}
			case MEDIUM: {
				minDuration = 40;
				break;
			}
			case HARD: {
				minDuration = 60;
				break;
			}
			default: {
				minDuration = 20;
			}
		}
		
		if (duration <= minDuration) return 4;
		if (duration <= (minDuration*1.5)) return 3;
		if (duration <= (minDuration*2)) return 2;
		return 1;		
	}
	
}


