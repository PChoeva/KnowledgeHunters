package knowledgehunters.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Person;
import knowledgehunters.model.Rank;
import knowledgehunters.model.Student;
import knowledgehunters.repository.StudentRepository;

@Service
public class StudentService {
	@Autowired StudentRepository studentRepository;
	@Autowired RankService rankService;
	
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
	
	public Student getStudentByPerson(Person person) {	
		for (Student student : studentRepository.findAll()) {
			if (student.getPerson().getId() == person.getId()) return student;
		}
		return null;
	}
	
	public void setStudentRankByCurrentPoints(Student student) {
		List<Rank> ranks = rankService.getAllRanks();
		
		System.out.println("Student RANK:" + student.getRank().getName() + "| min-points: + " + student.getRank().getMinPoints());
		
        Collections.reverse(ranks); 

		for (Rank rank : ranks) {
			System.out.println("RANK: " + rank.getName()+ " | " + rank.getMinPoints());
			if (student.getPoints() >= rank.getMinPoints()) {
				student.setRank(rank);	
				System.out.println(student.getRank().getName());
				break;
			}

		}
		
	}
}


