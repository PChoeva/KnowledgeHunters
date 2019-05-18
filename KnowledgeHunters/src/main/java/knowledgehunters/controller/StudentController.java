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

import knowledgehunters.model.Student;
import knowledgehunters.service.StudentService;


@RestController
public class StudentController {
	@Autowired
	private StudentService studentService;
	
	@GetMapping ("/students") 
	public List<Student> getAllStudents() {
		return  studentService.getAllStudents();
	}
	
	@GetMapping ("/students/{id}") 
	public Optional<Student> getStudentId(@PathVariable int id) {	
		return  studentService.getStudent(id);
	}
	
	@PostMapping(value="/students")
	public void addStudent(@RequestBody Student student){
		
		studentService.addStudent(student);	
	}
	
	@PutMapping(value ="/students/{id}")
	public void updateStudent(@RequestBody Student student,@PathVariable int id) {
		studentService.updateStudent(id, student);
	}
	
	@DeleteMapping(value ="/students/{id}")
	public void deleteStudent(@PathVariable int id) {
		studentService.deleteStudent(id);
	}
}
