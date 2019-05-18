package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Question;
import knowledgehunters.repository.QuestionRepository;

@Service
public class QuestionService {
	@Autowired QuestionRepository questionRepository;
	
	public List<Question> getAllQuestions(){
		List<Question> questions = new ArrayList<>();
		questionRepository.findAll().forEach(questions ::add);
		return questions;
	}
	
	public Optional<Question> getQuestion(int id){
		
		return  questionRepository.findById(id);
	}

	public void addQuestion(Question question) {
		
		questionRepository.save(question);
	}
	
	public void updateQuestion(int id, Question question) {
		
		questionRepository.save(question);
	}
	
	public void deleteQuestion(int id) {
		
		questionRepository.deleteById(id);
	}
}


