package knowledgehunters.repository;

import org.springframework.data.repository.CrudRepository;

import knowledgehunters.model.GameMove;

public interface GameMoveRepository extends CrudRepository<GameMove,Integer> {

}
