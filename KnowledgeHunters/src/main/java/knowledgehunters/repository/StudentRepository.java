package knowledgehunters.repository;

import org.springframework.data.repository.CrudRepository;

import knowledgehunters.model.Student;

public interface StudentRepository extends CrudRepository<Student,Integer> {

}
