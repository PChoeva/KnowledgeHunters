package knowledgehunters.repository;

import org.springframework.data.repository.CrudRepository;

import knowledgehunters.model.Ranking;

public interface RankingRepository extends CrudRepository<Ranking,Integer> {

}
