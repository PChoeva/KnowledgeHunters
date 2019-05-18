package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Student;
import knowledgehunters.repository.StudentRepository;

@Service
public class StudentService {
	@Autowired StudentRepository studentRepository;
	
	public List<Student> getAllStudents(){
		List<Student> students = new ArrayList<>();
		studentRepository.findAll().forEach(students ::add);
		return students;
	}
	
	public Optional<Student> getStudent(int id){
		
		return  studentRepository.findById(id);
	}

	public void addStudent(Student student) {
		
		studentRepository.save(student);
	}
	
	public void updateStudent(int id, Student student) {
		
		studentRepository.save(student);
	}
	
	public void deleteStudent(int id) {
		
		studentRepository.deleteById(id);
	}
}


