package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.GameMove;
import knowledgehunters.repository.GameMoveRepository;

@Service
public class GameMoveService {
	@Autowired GameMoveRepository gameMoveRepository;
	
	public List<GameMove> getAllGameMoves(){
		List<GameMove> gameMoves = new ArrayList<>();
		gameMoveRepository.findAll().forEach(gameMoves ::add);
		return gameMoves;
	}
	
	public Optional<GameMove> getGameMove(int id){
		
		return  gameMoveRepository.findById(id);
	}

	public void addGameMove(GameMove gameMove) {
		
		gameMoveRepository.save(gameMove);
	}
	
	public void updateGameMove(int id, GameMove gameMove) {
		
		gameMoveRepository.save(gameMove);
	}
	
	public void deleteGameMove(int id) {
		
		gameMoveRepository.deleteById(id);
	}
}


