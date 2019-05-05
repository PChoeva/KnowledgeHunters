package knowledgehunters.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import knowledgehunters.model.Lesson;
import knowledgehunters.service.LessonService;


@RestController
public class LessonController {
	@Autowired
	private LessonService lessonService;
	
	@GetMapping ("/lessons") 
	public List<Lesson> getAllLessons() {
		return  lessonService.getAllLessons();
	}
	
	@GetMapping ("/lessons/{id}") 
	public Optional<Lesson> getCityId(@PathVariable String id) {	
		return  lessonService.getLesson(id);
	}
	
	@PostMapping(value="/lessons")
	public void addLesson(@RequestBody Lesson lesson){
		
		lessonService.addLesson(lesson);	
	}
	
	@PutMapping(value ="/lessons/{id}")
	public void updateLesson(@RequestBody Lesson lesson,@PathVariable String id) {
		lessonService.updateLesson(id, lesson);
	}
	
	@DeleteMapping(value ="/lesson/{id}")
	public void deleteLesson(@PathVariable String id) {
		lessonService.deleteLesson(id);
	}
}
