package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Game;
import knowledgehunters.repository.GameRepository;

@Service
public class GameService {
	@Autowired GameRepository gameRepository;
	
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
}


