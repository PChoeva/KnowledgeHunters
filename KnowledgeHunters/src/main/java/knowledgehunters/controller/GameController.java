package knowledgehunters.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import knowledgehunters.model.Game;
import knowledgehunters.service.GameService;


@RestController
public class GameController {
	@Autowired
	private GameService gameService;
	
	@GetMapping ("/games") 
	public List<Game> getAllGames() {
		return  gameService.getAllGames();
	}
	
	@GetMapping ("/games/{id}") 
	public Optional<Game> getGameId(@PathVariable int id) {	
		return  gameService.getGame(id);
	}
	
	@PostMapping(value="/games")
	public void addGame(@RequestBody Game game){
		
		gameService.addGame(game);	
	}
	
	@PutMapping(value ="/games/{id}")
	public void updateGame(@RequestBody Game game,@PathVariable int id) {
		gameService.updateGame(id, game);
	}
	
	@DeleteMapping(value ="/games/{id}")
	public void deleteGame(@PathVariable int id) {
		gameService.deleteGame(id);
	}
}
