package knowledgehunters.repository;

import org.springframework.data.repository.CrudRepository;

import knowledgehunters.model.User;

public interface UserRepository extends CrudRepository<User,Integer> {

}
