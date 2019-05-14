package knowledgehunters.repository;

import org.springframework.data.repository.CrudRepository;

import knowledgehunters.model.Subject;

public interface SubjectRepository extends CrudRepository<Subject,Integer> {

}
