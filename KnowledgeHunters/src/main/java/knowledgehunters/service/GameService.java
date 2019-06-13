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
	
	public int calcGamePoints(Game game) {
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
	
}


