package knowledgehunters.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import knowledgehunters.enums.QuestionDifficulty;
import knowledgehunters.enums.QuestionType;
import knowledgehunters.model.Lesson;
import knowledgehunters.model.Option;
import knowledgehunters.model.Person;
import knowledgehunters.model.Question;
import knowledgehunters.model.Role;
import knowledgehunters.model.School;
import knowledgehunters.model.Topic;
import knowledgehunters.model.User;
import knowledgehunters.service.LessonService;
import knowledgehunters.service.OptionService;
import knowledgehunters.service.PersonService;
import knowledgehunters.service.QuestionService;
import knowledgehunters.service.UserService;


@RestController
public class BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private PersonService personService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private OptionService optionService;
	
	
	Person editedPerson;
	
	@PostMapping("/home")
	public void homeLogged(HttpSession session, HttpServletResponse response, @RequestParam("username") String username, @RequestParam("password") String password) throws IOException {
		System.out.println("---Entered home logged controller!");
		System.out.println("User:" + username);
		System.out.println("Pass:" + password);
		
		boolean isUserInDB = false;
		
		List<User> users = userService.getAllUsers();
		for(User user : users) {

	        System.out.println("DBUser:" + user.getUsername());
			System.out.println("DBPass:" + user.getPassword());

			System.out.println("Is Approved? " );
			// System.out.println("Is Approved: " + personService.findPersonByUserID(userService.findUser(username).getId()).getIsApproved());
			
			if (user.getUsername().equals(username) && user.getPassword().equals(password) && personService.findPersonByUserID(userService.findUser(username).getId()).getIsApproved()) 
			{
				System.out.println("in if for users");
				session.setAttribute("sessionUserID", user.getId());
				Person person = personService.findPersonByUserID(user.getId());
				personService.saveSessionPerson(person);
				
				System.out.println(session.getAttribute("sessionUserID"));
				session.setAttribute("sessionUsername", username);
				
				session.setAttribute("sessionDisplayName", person.getDisplayName());
				System.out.println("sessionGet: " + session.getAttribute("sessionDisplayName"));
				
		        session.setAttribute("sessionPassword", password);

		        isUserInDB = true;
			}
			
		}

		if (isUserInDB) {
			response.sendRedirect("/home"); 
		} else {
			session.setAttribute("errorMsg", "Невалидни данни!");
			response.sendRedirect("/login");
		}
	}
	
	@PostMapping("/profile")
	public void saveProfile(HttpSession session, HttpServletResponse response, @RequestParam("id") String id, @RequestParam("username") String username, @RequestParam("displayName") String displayName, @RequestParam("email") String email, @RequestParam("school") int school, @RequestParam("password") String password) throws IOException {
	
		System.out.println("---Entered saveProfile controller!");
		System.out.println("profile-id: " + id);
		System.out.println("profile-username: " + username);
		System.out.println("profile-displayName: " + displayName);
		System.out.println("profile-email: " + email);
		System.out.println("profile-school: " + school);
		System.out.println("profile-password: " + password);
		
		User user_old = userService.findUser(username);
		userService.updateUser(user_old.getId(), new User(user_old.getId(), username, password, user_old.getRole()));
		
	
		userService.getUser(user_old.getId()).ifPresent(
				user -> editedPerson = new Person(Integer.parseInt(id),user, new School(school,null, null), displayName, email));
		
		personService.updatePerson(Integer.parseInt(id), editedPerson);
		personService.getPerson(Integer.parseInt(id)).ifPresent(person -> personService.saveSessionPerson(person));
		
		session.setAttribute("sessionUsername", username);
		session.setAttribute("sessionDisplayName", displayName);
		session.setAttribute("successMsg", "Успешно запазване!");
		System.out.println("In profile POST: " + session.getAttribute("successMsg"));
		response.sendRedirect("/home"); 
	}
	
	@PostMapping("/change-password")
	public void saveProfile(HttpSession session, HttpServletResponse response, @RequestParam("password-old") String passwordОld, @RequestParam("password") String passwordNew, @RequestParam("password-repeat") String passwordNewRepeat) throws IOException {
		Person sessionPerson = personService.getSessionPerson();
		sessionPerson.getUser().setPassword(passwordNew);
		userService.updateUser(sessionPerson.getUser().getId(), sessionPerson.getUser());
		
		session.setAttribute("successMsg", "Успешна промяна на паролата!");
		response.sendRedirect("/home"); 
	}

	@PostMapping("/login")
	public void saveRegistrationAndLogin(HttpSession session, HttpServletResponse response, @RequestParam("username") String username, @RequestParam("displayName") String displayName, @RequestParam("email") String email, @RequestParam("school") int school, @RequestParam("password") String password, @RequestParam("isTeacher") boolean isTeacher) throws IOException {
			
		System.out.println("---Entered saveRegistrationAndLogin controller!");
		System.out.println(username + " | " + displayName + " | " + email + " | " + school + " | " + password);
		
		System.out.println("isTeacher: " + isTeacher);
		
		userService.addUser(new User(0, username, password, new Role(3,null)));
		personService.addPerson(new Person(0,userService.findUser(username), new School(school,null, null), displayName, email, !isTeacher));
		session.setAttribute("successMsg", "Успешно регистриране!");
		response.sendRedirect("/login"); 
	}
	
	@PostMapping("/lessons/index")
	  public void LessonsIndex(HttpSession session, HttpServletResponse response, @RequestParam("id") String lessonID, @RequestParam("title") String title, @RequestParam("topic") String topicID, @RequestParam("description") String description) throws IOException {
		Person person = personService.getSessionPerson();
	    System.out.println("REST Lessons index controller post");
	    
	    lessonService.addLesson(new Lesson(lessonID.isEmpty()?0:Integer.parseInt(lessonID), title, new Topic(Integer.parseInt(topicID), null, null),person, description));
	    session.setAttribute("successMsg", "Успешно запазване!");
	    response.sendRedirect("/lessons/index");
	}
	
	@PostMapping("/questions/index")
	  public void questionsIndex(
			  HttpSession session,
			  HttpServletResponse response,
			  @RequestParam("id") String questionID,
			  @RequestParam("title") String description,
			  @RequestParam("difficulty") String difficulty,
			  @RequestParam("authorID") String authorID,
			  @RequestParam("hint") String hint,
			  @RequestParam("topic") String topicID,
			  @RequestParam("type") String type,
			  
			  @RequestParam(value = "radio-multiple-choice-filled", required=false) String radioMultipleFilled,
			  @RequestParam(value = "radio-multiple-choice-empty", required=false) String radioMultipleEmpty,
			  @RequestParam(value = "radio-true-false-filled", required=false) String radioTrueFalseFilled,
			  @RequestParam(value = "radio-true-false-empty", required=false) String radioTrueFalseEmpty,
			  @RequestParam(value = "radio-open-filled", required=false) String radioOpenFilled,
			  @RequestParam(value = "radio-open-empty", required=false) String radioOpenEmpty,
			
			  @RequestParam(value = "multiple-option-filled[]", required=false) List<String> optionMultipleFilled,
			  @RequestParam(value = "multiple-option-empty[]", required=false) List<String> optionMultipleEmpty,
			  @RequestParam(value = "true-false-option-filled[]", required=false) List<String> optionTrueFalseFilled,
			  @RequestParam(value = "true-false-option-empty[]", required=false) List<String> optionTrueFalseEmpty,
			  @RequestParam(value = "open-option-filled[]", required=false) List<String> optionOpenFilled,
			  @RequestParam(value = "open-option-empty[]", required=false) List<String> optionOpenEmpty,
			  
			  @RequestParam(value = "multiple-index-filled[]", required=false) List<String> indexMultipleFilled,
			  @RequestParam(value = "multiple-index-empty[]", required=false) List<String> indexMultipleEmpty,
			  @RequestParam(value = "true-false-index-filled[]", required=false) List<String> indexTrueFalseFilled,
			  @RequestParam(value = "true-false-index-empty[]", required=false) List<String> indexTrueFalseEmpty,
			  @RequestParam(value = "open-index-filled[]", required=false) List<String> indexOpenFilled,
			  @RequestParam(value = "open-index-empty[]", required=false) List<String> indexOpenEmpty
		) throws IOException {
		
		Person person = personService.getSessionPerson();
	    System.out.println("REST Questions index controller post");
	    System.out.println(questionID + " | " + description + " | " + difficulty + " | " + authorID + " | " + hint + " | " + topicID + " | " + type);
	    
	    switch(type) {
	  		case "MULTIPLE_CHOICE": {
	  			System.out.println("Switch: multiple");
	  			System.out.println("radio Multiple filled:" + radioMultipleFilled);
		    	System.out.println("radio Multiple empty:" + radioMultipleEmpty);
		    	
		    	if (radioMultipleEmpty != null) {

		    		System.out.println("SWITCH CASE optionOpenEmpty: " + optionMultipleEmpty);
		    		addQuestion((questionID != null && !questionID.equals(""))? Integer.parseInt(questionID): 0, description, difficulty, hint, type, Integer.parseInt(topicID), Integer.parseInt(authorID), indexMultipleEmpty, optionMultipleEmpty, radioMultipleEmpty);		    		
		    		break;
		    	}
		    	
		    	if (radioMultipleFilled != null) {
		    		System.out.println("IN IF, authorID:" + authorID);
		    		editQuestion(Integer.parseInt(questionID), description, difficulty, hint, type, Integer.parseInt(topicID), Integer.parseInt(authorID), indexMultipleFilled, optionMultipleFilled, radioMultipleFilled);
			    	break;
		    	}
		    	
		    	System.out.println("option Multiple empty:" + optionMultipleEmpty);
	  			break;
	  		}
	  		case "TRUE_FALSE": {	
	  			System.out.println("Switch: true-false");
	  			System.out.println("radio True False filled:" + radioTrueFalseFilled);
		    	System.out.println("radio True False empty:" + radioTrueFalseEmpty);
		    	
		    	System.out.println("option True False filled:" + optionTrueFalseFilled);
		    	System.out.println("option True False empty:" + optionTrueFalseEmpty);
		    	
		    	if (radioTrueFalseEmpty != null) {
		    		addQuestion((questionID != null && !questionID.equals(""))? Integer.parseInt(questionID): 0, description, difficulty, hint, type, Integer.parseInt(topicID), Integer.parseInt(authorID), indexTrueFalseEmpty, optionTrueFalseEmpty, radioTrueFalseEmpty);		    		
		    		break;
		    	}
		    	
		    	if (radioTrueFalseFilled != null) {
		    		editQuestion(Integer.parseInt(questionID), description, difficulty, hint, type, Integer.parseInt(topicID), Integer.parseInt(authorID), indexTrueFalseFilled, optionTrueFalseFilled, radioTrueFalseFilled);
			    	break;
		    	}
		    	
	  			break;
	  		}
	  		case "OPEN": {
	  			System.out.println("Switch: open");
	  			System.out.println("radio Open filled:" + radioOpenFilled);
		    	System.out.println("radio Open empty:" + radioOpenEmpty);
		    	
		    	System.out.println("===========ADD MODE===========");
		    	System.out.println("radioOpenEmpty: <" + radioOpenEmpty.getClass().getName() + ">:" + radioOpenEmpty);
		    	if (radioOpenEmpty != null) {
		    		addQuestion((questionID != null && !questionID.equals(""))? Integer.parseInt(questionID): 0, description, difficulty, hint, type, Integer.parseInt(topicID), Integer.parseInt(authorID), indexOpenEmpty, optionOpenEmpty, radioOpenEmpty);
		    		break;
		    	}
		    	
		    	System.out.println("===========EDIT MODE===========");
		    	if (radioOpenFilled != null) {
		    		editQuestion(Integer.parseInt(questionID), description, difficulty, hint, type, Integer.parseInt(topicID), Integer.parseInt(authorID), indexOpenFilled, optionOpenFilled, radioOpenFilled);
			    	break;
		    	}

	  			break;
	  		}
	  		case "MATCH": break;
	  		case "SORT": break;
	    }
	    
	    session.setAttribute("successMsg", "Успешно запазване!");
	    response.sendRedirect("/questions/index");
	}
	
	
	public void addQuestion(int questionID, String description, String difficulty, String hint, String type, int topicID, int authorID, List<String> indexOpenEmpty, List<String> optionOpenEmpty, String radioOpenEmpty) {
		System.out.println("IN ADD??");
		if(questionID != 0) 	{
			System.out.println("Question ID: " + questionID	);
			System.out.println("Open empty in questionID!=null if");
    		List<Option> options = optionService.getAllOptionsByQuestionId(questionID);
    		for (Option option : options) {
    			System.out.println("Open empty in questionID!=null if for delete");
    			optionService.deleteOption(option.getId());
    		}
    	
    	}
		System.out.println("Open empty between ifs");
		System.out.println("optionOpenEmpty: " + optionOpenEmpty);
		Question question = null;
		if (questionID !=0) {
			question = questionService.updateAndGetQuestion(questionID, new Question(questionID, description, QuestionDifficulty.valueOf(difficulty), personService.getSessionPerson(), hint.equals("") ? null: hint, new Topic(topicID, null, null), QuestionType.valueOf(type)));
		} else {
			question = new Question(0, description, QuestionDifficulty.valueOf(difficulty), personService.getSessionPerson(), hint.equals("") ? null: hint, new Topic(topicID, null, null), QuestionType.valueOf(type));
			question = questionService.saveQuestion(question);
		}
		
		for(int index = 0; index < optionOpenEmpty.size(); index++) {

			System.out.println("Option: " + optionOpenEmpty.get(index));
			
			System.out.println("After question, before addOption:");
			System.out.println("After question, before addOption authorID:" + authorID);
			System.out.println("After question, before addOption questionID:" + question.getId());
			
			boolean isCorrect = false;
			if (index == Integer.parseInt(radioOpenEmpty)) {
				isCorrect = true;
			}
			optionService.addOption(new Option(0, question, optionOpenEmpty.get(index), isCorrect, 0, null));
		}
		
	}

	public void editQuestion(int questionID, String description, String difficulty, String hint, String type, int topicID, int authorID, List<String> indexOpenFilled, List<String> optionOpenFilled, String radioOpenFilled) {
		System.out.println("IN editQuestion, authorID:" + authorID);
		List<Option> options = optionService.getAllOptionsByQuestionId(questionID);
		
		System.out.println("EDIT TEST new: " + type);
		System.out.println("EDIT TEST from question: " + questionService.getQuestion(questionID).get().getType());
		
		Question updatedQuestion = questionService.updateAndGetQuestion(questionID, new Question(questionID, description, QuestionDifficulty.valueOf(difficulty), personService.getSessionPerson(), hint.equals("") ? null: hint, new Topic(topicID, null, null), QuestionType.valueOf(type)));
    	System.out.println("QuestionType EDIT:" + QuestionType.valueOf(type));
    	
		
		if (!questionService.getQuestion(questionID).get().getType().equals(type)) {
			for (Option option: options) {
				System.out.println("DELETE OPTION: " + option.getId());
				optionService.deleteOption(option.getId());
				
				for(int index = 0; index < optionOpenFilled.size(); index++) {
				
					System.out.println("Option: " + optionOpenFilled.get(index));
					
					System.out.println("After question, before addOption:");
					System.out.println("After question, before addOption authorID:" + authorID);
					System.out.println("After question, before addOption questionID:" + updatedQuestion.getId());
					
					boolean isCorrect = false;
					if (index == Integer.parseInt(radioOpenFilled)) {
						isCorrect = true;
					}
					optionService.addOption(new Option(0, updatedQuestion, optionOpenFilled.get(index), isCorrect, 0, null));
					
					
					System.out.println("Unreachable?");
				}
			}
		} else {
			for (Option option : options) {
	    		for(int index = 0; index < indexOpenFilled.size(); index++) {
	    			if (option.getId() == Integer.parseInt(indexOpenFilled.get(index))) {
	    				option.setDescription(optionOpenFilled.get(index));
	    				option.setCorrect(option.getId() == Integer.parseInt(radioOpenFilled));
	    				optionService.updateOption(option.getId(), option);
	    			}
	    		}
	    	}
		}

    	for (String option : optionOpenFilled) {
	    	System.out.println("option Open filled:" + option);
    	}
    	for (String index : indexOpenFilled) {
	    	System.out.println("index Open filled:" + index);
    	}
	}
	
}
