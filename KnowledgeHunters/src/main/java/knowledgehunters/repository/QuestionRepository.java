package knowledgehunters.repository;

import org.springframework.data.repository.CrudRepository;

import knowledgehunters.model.Question;

public interface QuestionRepository extends CrudRepository<Question,Integer> {

}
