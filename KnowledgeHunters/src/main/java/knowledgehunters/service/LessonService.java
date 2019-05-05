package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Lesson;
import knowledgehunters.repository.LessonRepository;

@Service
public class LessonService {
	@Autowired LessonRepository lessonRepository;
	
	public List<Lesson> getAllLessons(){
		List<Lesson> lessons = new ArrayList<>();
		lessonRepository.findAll().forEach(lessons ::add);
		return lessons;
	}
	
	public Optional<Lesson> getLesson(String id){
		
		return  lessonRepository.findById(id);
	}

	public void addLesson(Lesson lesson) {
		
		lessonRepository.save(lesson);
	}
	
	public void updateLesson(String id, Lesson lesson) {
		
		lessonRepository.save(lesson);
	}
	
	public void deleteLesson(String id) {
		
		lessonRepository.deleteById(id);
	}
}


