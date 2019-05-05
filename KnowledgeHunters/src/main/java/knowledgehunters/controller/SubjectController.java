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

import knowledgehunters.model.Subject;
import knowledgehunters.service.SubjectService;


@RestController
public class SubjectController {
	@Autowired
	private SubjectService subjectService;
	
	@GetMapping ("/subjects") 
	public List<Subject> getAllSubjects() {
		return  subjectService.getAllSubjects();
	}
	
	@GetMapping ("/subjects/{id}") 
	public Optional<Subject> getSubjectId(@PathVariable String id) {	
		return  subjectService.getSubject(id);
	}
	
	@PostMapping(value="/subjects")
	public void addSubject(@RequestBody Subject subject){
		
		subjectService.addSubject(subject);	
	}
	
	@PutMapping(value ="/subjects/{id}")
	public void updateSubject(@RequestBody Subject subject,@PathVariable String id) {
		subjectService.updateSubject(id, subject);
	}
	
	@DeleteMapping(value ="/subjects/{id}")
	public void deleteSubject(@PathVariable String id) {
		subjectService.deleteSubject(id);
	}
}
