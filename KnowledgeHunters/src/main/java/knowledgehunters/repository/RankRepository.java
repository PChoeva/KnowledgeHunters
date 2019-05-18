package knowledgehunters.repository;

import org.springframework.data.repository.CrudRepository;

import knowledgehunters.model.Rank;

public interface RankRepository extends CrudRepository<Rank,Integer> {

}
