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
import knowledgehunters.model.Question;
import knowledgehunters.enums.QuestionDifficulty;
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
	
	public List<Question> getAllQuestionsByAuthor(Person author){
		List<Question> questionsByAuthor = new ArrayList<>();
		System.out.println("getAllQuestionsByAuthor: " + questionRepository.findAll());
		for (Question question: questionRepository.findAll()) {
			if (question.getAuthor().getId() == author.getId()) {
				questionsByAuthor.add(question);
			}
		}
		questionsByAuthor.forEach(q -> System.out.println("getAllQuestionsByAuthor -> Question: " + q));

		return sortedQuestions(questionsByAuthor);
	}
		
	public Question saveQuestion(Question question) {
	    return questionRepository.save(question);
	}
	
	public Question updateAndGetQuestion(int id, Question question) {
		
		return questionRepository.save(question);
	}
	
	public List<Question> getQuestionsByDifficultyAndSubjectId(QuestionDifficulty difficulty, int subjectId){
		System.out.println("IN getQuestionsByDifficultyAndSubjectId:");
		List<Question> filteredQuestions = new ArrayList<>();
		for (Question question : questionRepository.findAll()) {
			
			System.out.println("DIFFICLUTY: "+ difficulty.getValue());
			System.out.println("SUBJECT ID: " + subjectId);
			System.out.println("DIFFICLUTY: "+ difficulty.getValue());
			
			if (question.getDifficulty().equals(difficulty) && question.getTopic().getSubject().getId() == subjectId) {
				filteredQuestions.add(question);
				System.out.println("In loop and if");
			}
		}
		filteredQuestions.forEach(q -> System.out.println(q.getDescription()));
		return filteredQuestions;
	}
	
	public List<Question> sortedQuestions(List<Question> questions) {
		Collections.sort(questions, new Comparator<Question>(){
		    public int compare(Question q1, Question q2) {
		    	if (q1.getDescription().compareToIgnoreCase(q2.getDescription()) > 0) {
		    		return 1;
		    	}
		    	if (q1.getDescription().compareToIgnoreCase(q2.getDescription()) < 0) {
		    		return -1;
		    	}
		        return 0;
		    }
		});
		return questions;
	}
}


