package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Subject;
import knowledgehunters.repository.SubjectRepository;

@Service
public class SubjectService {
	@Autowired SubjectRepository subjectRepository;
	
	public List<Subject> getAllSubjects(){
		List<Subject> subjects = new ArrayList<>();
		subjectRepository.findAll().forEach(subjects ::add);
		return subjects;
	}
	
	public Optional<Subject> getSubject(String id){
		
		return  subjectRepository.findById(id);
	}

	public void addSubject(Subject subject) {
		
		subjectRepository.save(subject);
	}
	
	public void updateSubject(String id, Subject subject) {
		
		subjectRepository.save(subject);
	}
	
	public void deleteSubject(String id) {
		
		subjectRepository.deleteById(id);
	}
}


