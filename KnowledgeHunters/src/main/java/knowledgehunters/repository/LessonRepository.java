package knowledgehunters.repository;

import org.springframework.data.repository.CrudRepository;

import knowledgehunters.model.Lesson;

public interface LessonRepository extends CrudRepository<Lesson,String> {

}
