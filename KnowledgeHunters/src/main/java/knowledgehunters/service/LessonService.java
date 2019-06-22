package knowledgehunters.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Lesson;
import knowledgehunters.model.Person;
import knowledgehunters.model.Student;
import knowledgehunters.repository.LessonRepository;

@Service
public class LessonService {
	@Autowired LessonRepository lessonRepository;
	
	public List<Lesson> getAllLessons(){
		List<Lesson> lessons = new ArrayList<>();
		lessonRepository.findAll().forEach(lessons ::add);
		return lessons;
	}
	
	public Optional<Lesson> getLesson(int id){
		
		return  lessonRepository.findById(id);
	}

	public void addLesson(Lesson lesson) {
		
		lessonRepository.save(lesson);
	}
	
	public void updateLesson(int id, Lesson lesson) {
		
		lessonRepository.save(lesson);
	}
	
	public void deleteLesson(int id) {
		
		lessonRepository.deleteById(id);
	}
	
	public List<Lesson> getAllLessonsByAuthor(Person author){
		List<Lesson> lessonsByAuthor = new ArrayList<>();
		System.out.println("getAllLessonsByAuthor: " + lessonRepository.findAll());
		for (Lesson lesson: lessonRepository.findAll()) {
			if (lesson.getAuthor().getId() == author.getId()) {
				lessonsByAuthor.add(lesson);
			}
		}
		lessonsByAuthor.forEach(l -> System.out.println("getAllLessonsByAuthor -> Lesson: " + l));

		return sortedLessons(lessonsByAuthor);
	}
	
	public List<Lesson> getAllLessonsBySubjectId(int subjectId){
		List<Lesson> lessonsBySubject = new ArrayList<>();
		System.out.println("getAllLessonsByAuthor: " + lessonRepository.findAll());
		for (Lesson lesson: lessonRepository.findAll()) {
			if (lesson.getTopic().getSubject().getId() == subjectId) {
				lessonsBySubject.add(lesson);
			}
		}
		lessonsBySubject.forEach(l -> System.out.println("getAllLessonsBySubjectId -> Lesson: " + l));

		return sortedLessons(lessonsBySubject);
	}
	
	public List<Lesson> sortedLessons(List<Lesson> lessons) {
		Collections.sort(lessons, new Comparator<Lesson>(){
		    public int compare(Lesson lesson1, Lesson lesson2) {
		    	if (lesson1.getTitle().compareToIgnoreCase(lesson2.getTitle()) > 0) {
		    		return 1;
		    	}
		    	if (lesson1.getTitle().compareToIgnoreCase(lesson2.getTitle()) < 0) {
		    		return -1;
		    	}
		        return 0;
		    }
		});
		return lessons;
	}
	
}


