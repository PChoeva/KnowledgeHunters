package knowledgehunters.repository;

import org.springframework.data.repository.CrudRepository;

import knowledgehunters.model.Game;

public interface GameRepository extends CrudRepository<Game,Integer> {

}
