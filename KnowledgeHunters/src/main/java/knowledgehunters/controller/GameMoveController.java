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

import knowledgehunters.model.GameMove;
import knowledgehunters.service.GameMoveService;


@RestController
public class GameMoveController {
	@Autowired
	private GameMoveService gameMoveService;
	
	@GetMapping ("/gamemoves") 
	public List<GameMove> getAllGameMoves() {
		return  gameMoveService.getAllGameMoves();
	}
	
	@GetMapping ("/gamemoves/{id}") 
	public Optional<GameMove> getGameMoveId(@PathVariable int id) {	
		return  gameMoveService.getGameMove(id);
	}
	
	@PostMapping(value="/gamemoves")
	public void addGameMove(@RequestBody GameMove gameMove){
		gameMoveService.addGameMove(gameMove);	
	}
	
	@PutMapping(value ="/gamemoves/{id}")
	public void updateGameMove(@RequestBody GameMove gameMove,@PathVariable int id) {
		gameMoveService.updateGameMove(id, gameMove);
	}
	
	@DeleteMapping(value ="/gamemoves/{id}")
	public void deleteGameMove(@PathVariable int id) {
		gameMoveService.deleteGameMove(id);
	}
}
