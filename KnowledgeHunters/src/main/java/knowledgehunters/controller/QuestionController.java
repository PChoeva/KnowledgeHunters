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

import knowledgehunters.model.Question;
import knowledgehunters.service.QuestionService;


@RestController
public class QuestionController {
	@Autowired
	private QuestionService questionService;
	
	@GetMapping ("/questions") 
	public List<Question> getAllQuestions() {
		return  questionService.getAllQuestions();
	}
	
	@GetMapping ("/questions/{id}") 
	public Optional<Question> getQuestionId(@PathVariable int id) {	
		return  questionService.getQuestion(id);
	}
	
	@PostMapping(value="/questions")
	public void addQuestion(@RequestBody Question question){
		
		questionService.addQuestion(question);	
	}
	
	@PutMapping(value ="/questions/{id}")
	public void updateQuestion(@RequestBody Question question,@PathVariable int id) {
		questionService.updateQuestion(id, question);
	}
	
	@DeleteMapping(value ="/questions/{id}")
	public void deleteQuestion(@PathVariable int id) {
		questionService.deleteQuestion(id);
	}
}
