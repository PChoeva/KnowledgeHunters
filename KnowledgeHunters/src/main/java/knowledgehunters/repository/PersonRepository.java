package knowledgehunters.repository;

import org.springframework.data.repository.CrudRepository;

import knowledgehunters.model.Person;

public interface PersonRepository extends CrudRepository<Person,String> {

}
