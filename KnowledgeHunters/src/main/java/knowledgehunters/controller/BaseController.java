package knowledgehunters.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import knowledgehunters.enums.QuestionDifficulty;
import knowledgehunters.enums.QuestionType;
import knowledgehunters.model.Game;
import knowledgehunters.model.GameMove;
import knowledgehunters.model.Lesson;
import knowledgehunters.model.Option;
import knowledgehunters.model.Person;
import knowledgehunters.model.Question;
import knowledgehunters.model.Rank;
import knowledgehunters.model.Ranking;
import knowledgehunters.model.Role;
import knowledgehunters.model.School;
import knowledgehunters.model.Student;
import knowledgehunters.model.Topic;
import knowledgehunters.model.User;
import knowledgehunters.service.GameMoveService;
import knowledgehunters.service.GameService;
import knowledgehunters.service.LessonService;
import knowledgehunters.service.OptionService;
import knowledgehunters.service.PersonService;
import knowledgehunters.service.QuestionService;
import knowledgehunters.service.RankingService;
import knowledgehunters.service.RoleService;
import knowledgehunters.service.StudentService;
import knowledgehunters.service.SubjectService;
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
	private SubjectService subjectService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private GameService gameService;
	@Autowired
	private GameMoveService gameMoveService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RankingService rankingService;
	
	
	Person editedPerson;
	
	@PostMapping("/home")
	public void homeLogged(
		HttpSession session,
		HttpServletResponse response,
		@RequestParam("username") String username,
		@RequestParam("password") String password
	) throws IOException {
		
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
			
			if (user.getUsername().equals(username) && user.getPassword().equals(password) &&
				personService.findPersonByUserID(userService.findUser(username).getId()).getIsApproved()){
				
				System.out.println("in if for users");
				session.setAttribute("sessionUserID", user.getId());
				Person person = personService.findPersonByUserID(user.getId());
				personService.saveSessionPerson(person);
				
				if (person.getUser().getRole().getName().equals("Student")) {
					Student student = studentService.getStudentByPerson(person);
					session.setAttribute("sessionStudentRank", student.getRank());
				}
				
				
				
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
	
	@PostMapping("/game/filters")
	public void setGameFilters(
		HttpSession session,
		HttpServletResponse response,
		@RequestParam("difficulty") String difficulty,
		@RequestParam("subject") String subject
	) throws IOException {
		
		System.out.println("====Game Filters====");
		System.out.println("Post FILTERS difficulty:" + difficulty);
		System.out.println("Post FILTERS subject:" + subject);
		System.out.println();
		session.setAttribute("gameDifficulty", difficulty);
		session.setAttribute("gameSubject", subject);
		
		response.sendRedirect("/game-move");
	}
	
	@PostMapping("/game")
	public void game(
			HttpSession session,
			HttpServletResponse response,
			HttpServletRequest request,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("duration") String duration
			) throws IOException {
System.out.println("Start game timestamp: " + timestamp);
System.out.println("Game duration: " + duration);
		
		String[] questionsIds = request.getParameterValues("questions[]");
		List<Question> questions = new ArrayList<>();

		for (String questionId : questionsIds) {
			questionService.getQuestion(Integer.parseInt(questionId)).ifPresent(q ->questions.add(q));
		}
		
		Game game = gameService.saveGame(new Game(0, timestamp, Integer.parseInt(duration)));
		java.util.Date time=new java.util.Date(Long.parseLong(timestamp)*1000);
		
		LocalDateTime dateNow = LocalDateTime.now();
//		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss - E, dd MM yyyy").withLocale(Locale.forLanguageTag("bg-BG")); 
		DateTimeFormatter dateNowFormatted = DateTimeFormatter.ofPattern("HH:mm:ss").withLocale(Locale.forLanguageTag("bg-BG"));

		String formattedDate = dateNow.format(dateNowFormatted);
		
		session.setAttribute("startDate", formattedDate);
		session.setAttribute("duration", duration);
		
		
		
		
		System.out.println("===In /game post controller===");
		questions.forEach(q -> System.out.println(q.getId() + " | " + q.getDescription()));

		for (Question question : questions) {
			if (question.getType() == QuestionType.OPEN) {
				String optionStr = request.getParameter("option-" + question.getId());
				
				gameMoveService.addGameMove(new GameMove(0, game, studentService.getStudentByPerson(personService.getSessionPerson()), question, null, optionStr, 0, null));
				System.out.println("Option: " + optionStr);
			} else {
				String radio = request.getParameter("radio-" + question.getId());
				optionService
					.getOption(Integer.parseInt(radio))
					.ifPresent(answer -> gameMoveService.addGameMove(
							new GameMove(
									0,
									game,
									studentService.getStudentByPerson(personService.getSessionPerson()),
									question,
									answer,
									null,
									0,
									null
							)
				));
				System.out.println("Radio: " + radio);
			}
		}
		int gamePoints = gameService.calcGamePoints(game, Integer.parseInt(duration));

		if (personService.getSessionPerson().isStudent() &&  studentService.getStudentByPerson(personService.getSessionPerson()) != null) {
			Student currentStudent = studentService.getStudentByPerson(personService.getSessionPerson());
			System.out.println("Curr Student: " + currentStudent.getPerson().getDisplayName());
			currentStudent.setPoints(currentStudent.getPoints() + gamePoints);
			

			studentService.setStudentRankByCurrentPoints(currentStudent);
			studentService.updateStudent(currentStudent.getId(), currentStudent);
			
			int subjectId = Integer.parseInt((String) session.getAttribute("gameSubject"));
			Ranking createdRanking = rankingService.findRankingBySubjectIdAndStudent(subjectId, currentStudent);
			if (createdRanking == null) {
				subjectService.getSubject(subjectId).ifPresent(subject -> rankingService.addRanking(new Ranking(0, subject, currentStudent, gamePoints)));				
			} else {
				createdRanking.setPoints(createdRanking.getPoints() + gamePoints);
				rankingService.updateRanking(createdRanking.getId(), createdRanking);
			}
			
			
		}
				
		session.setAttribute("gamePoints", gamePoints);
		session.setAttribute("gameId", game.getId());
		response.sendRedirect("/game/review");
	}

	@PostMapping("/profile")
	public void saveProfile(
		HttpSession session,
		HttpServletResponse response,
		@RequestParam("id") String id,
		@RequestParam("username") String username,
		@RequestParam("displayName") String displayName,
		@RequestParam("email") String email,
		@RequestParam("school") int school,
		@RequestParam("password") String password
	) throws IOException {
	
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
	public void savePassword(
		HttpSession session,
		HttpServletResponse response,
		@RequestParam("password-old") String passwordОld,
		@RequestParam("password") String passwordNew,
		@RequestParam("password-repeat") String passwordNewRepeat
	) throws IOException {
		
		Person sessionPerson = personService.getSessionPerson();
		sessionPerson.getUser().setPassword(passwordNew);
		userService.updateUser(sessionPerson.getUser().getId(), sessionPerson.getUser());
		
		session.setAttribute("successMsg", "Успешна промяна на паролата!");
		response.sendRedirect("/home"); 
	}

	@PostMapping("/login")
	public void saveRegistrationAndLogin(
		HttpSession session,
		HttpServletResponse response,
		@RequestParam("username") String username,
		@RequestParam("displayName") String displayName,
		@RequestParam("email") String email,
		@RequestParam("school") int school,
		@RequestParam("password") String password,
		@RequestParam(value = "isTeacher", required=false) boolean isTeacher
	) throws IOException {
	
		if ((username.equals("") || username.equals(null)) || displayName.equals("") || email.equals("") || password.equals("")) {
			session.setAttribute("errorMsg", "Моля, попълнете всички полета!");
			response.sendRedirect("/register"); 
			return;
		} else
		
		if (userService.findUser(username) != null) {
			session.setAttribute("errorMsg", "Потребителското име " + username + " е заето!");
			response.sendRedirect("/register");
			return;
		}
		
		System.out.println("---Entered saveRegistrationAndLogin controller!");
		System.out.println("Username type:" + username.getClass().getName() + "|" + username + " | " + displayName + " | " + email + " | " + school + " | " + password);
		
		System.out.println("isTeacher: " + isTeacher);
		
		if (isTeacher) {
			roleService.getRole(2).ifPresent(role -> userService.addUser(new User(0, username, password, role)));
		} else {
			roleService.getRole(3).ifPresent(role -> userService.addUser(new User(0, username, password, role)));
		}
		System.out.println("User name: " + userService.findUser(username).getUsername());
		System.out.println("User role: " + userService.findUser(username).getRole().getName());
		Person registeredPerson = personService.savePerson(new Person(0, userService.findUser(username), new School(school,null, null), displayName, email, !isTeacher));
		System.out.println("registered Person Name: " + registeredPerson.getDisplayName());
		System.out.println("registered Person Role: " + registeredPerson.getUser().getRole().getName());
		if (registeredPerson.isStudent())	{
			studentService.addStudent(new Student(0, registeredPerson, new Rank(1,null, 0), 0));
		}
		
		session.setAttribute("successMsg", "Успешно регистриране!");
		response.sendRedirect("/login"); 
	}
	
	@PostMapping("/lessons/index")
	  public void LessonsIndex(
		HttpSession session,
		HttpServletResponse response,
		@RequestParam("id") String lessonID,
		@RequestParam("title") String title,
		@RequestParam("topic") String topicID,
		@RequestParam("description") String description
	) throws IOException {
		
		Person person = personService.getSessionPerson();
	    System.out.println("REST Lessons index controller post");
	    
	    lessonService.addLesson(new Lesson(lessonID.isEmpty()?0:Integer.parseInt(lessonID), title, new Topic(Integer.parseInt(topicID), null, null),person, description));
	    session.setAttribute("successMsg", "Успешно запазване!");
	    response.sendRedirect("/lessons/index");
	}
	
	@PostMapping("/questions/index")
	  public void questionsIndex(
			  HttpSession session,
			  HttpServletRequest request,
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
		
		System.out.println("Req Method: "+ request.getMethod());
		System.out.println("Req Param filled: "+ request.getParameter("radio-open-filled"));
		System.out.println("Req Param empty: "+ request.getParameter("radio-open-empty"));
		Enumeration<String> lst = request.getParameterNames();
		while (lst.hasMoreElements()) {
			String name = lst.nextElement();
			System.out.print(name + ": [");
			String[] vals = request.getParameterValues(name);
			for(String val : vals) {
				System.out.print(val + ", ");
			}
			System.out.println("]");
		}
		
		System.out.println("PRINT radioOpenFilled:" + radioOpenFilled);
		System.out.println("PRINT radioOpenEmpty:" + radioOpenEmpty);
		
		
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
		    		addQuestion(
	    				(questionID != null && !questionID.equals(""))? Integer.parseInt(questionID): 0,
	    				description,
	    				difficulty,
	    				hint,
	    				type,
	    				Integer.parseInt(topicID),
	    				Integer.parseInt(authorID),
	    				indexMultipleEmpty,
	    				optionMultipleEmpty,
	    				radioMultipleEmpty
	    			);		    		
		    		break;
		    	}
		    	
		    	if (radioMultipleFilled != null) {
		    		System.out.println("IN IF, authorID:" + authorID);
		    		editQuestion(
	    				Integer.parseInt(questionID),
	    				description,
	    				difficulty,
	    				hint,
	    				type,
	    				Integer.parseInt(topicID),
	    				Integer.parseInt(authorID),
	    				indexMultipleFilled,
	    				optionMultipleFilled,
	    				radioMultipleFilled
	    			);
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
		    		addQuestion(
		    			(questionID != null && !questionID.equals(""))? Integer.parseInt(questionID): 0,
		    			description,
		    			difficulty,
		    			hint,
		    			type,
		    			Integer.parseInt(topicID),
		    			Integer.parseInt(authorID),
		    			indexTrueFalseEmpty,
		    			optionTrueFalseEmpty,
		    			radioTrueFalseEmpty
		    		);		    		
		    		break;
		    	}
		    	
		    	if (radioTrueFalseFilled != null) {
		    		editQuestion(
		    			Integer.parseInt(questionID),
		    			description,
		    			difficulty,
		    			hint,
		    			type,
		    			Integer.parseInt(topicID),
		    			Integer.parseInt(authorID),
		    			indexTrueFalseFilled,
		    			optionTrueFalseFilled,
		    			radioTrueFalseFilled
		    		);
			    	break;
		    	}
		    	
	  			break;
	  		}
	  		case "OPEN": {
	  			System.out.println("Switch: open");
	  			System.out.println("radio Open filled:" + radioOpenFilled);
		    	System.out.println("radio Open empty:" + radioOpenEmpty);
		    	
		    	System.out.println("===========ADD MODE===========");
		    	if (radioOpenEmpty != null) {
		    		addQuestion(
		    			(questionID != null && !questionID.equals(""))? Integer.parseInt(questionID): 0,
		    			description,
		    			difficulty,
		    			hint,
		    			type,
		    			Integer.parseInt(topicID),
		    			Integer.parseInt(authorID),
		    			indexOpenEmpty,
		    			optionOpenEmpty,
		    			radioOpenEmpty
		    		);
		    		break;
		    	}
		    	
		    	System.out.println("===========EDIT MODE===========");
		    	if (radioOpenFilled != null) {
		    		editQuestion(
		    			Integer.parseInt(questionID),
		    			description,
		    			difficulty,
		    			hint,
		    			type,
		    			Integer.parseInt(topicID),
		    			Integer.parseInt(authorID),
		    			indexOpenFilled,
		    			optionOpenFilled,
		    			radioOpenFilled
		    		);
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
	
	
	public void addQuestion(
		int questionID,
		String description,
		String difficulty,
		String hint,
		String type,
		int topicID,
		int authorID,
		List<String> indexEmpty,
		List<String> optionEmpty,
		String radioEmpty
	) {
		
		if(questionID != 0) {
    		List<Option> options = optionService.getAllOptionsByQuestionId(questionID);
    		for (Option option : options) {
    			optionService.deleteOption(option.getId());
    		}
    	
    	}
		Question question = null;
		if (questionID !=0) {
			question = questionService.updateAndGetQuestion(
				questionID,
				new Question(
					questionID,
					description,
					QuestionDifficulty.valueOf(difficulty),
					personService.getSessionPerson(),
					hint.equals("") ? null: hint,
					new Topic(topicID, null, null),
					QuestionType.valueOf(type)
				)
			);
		} else {
			question = new Question(
				0,
				description,
				QuestionDifficulty.valueOf(difficulty),
				personService.getSessionPerson(),
				hint.equals("") ? null: hint,
				new Topic(topicID, null, null),
				QuestionType.valueOf(type)
			);
			question = questionService.saveQuestion(question);
		}
		
		for(int index = 0; index < optionEmpty.size(); index++) {			
			boolean isCorrect = false;
			if (index == Integer.parseInt(radioEmpty)) {
				isCorrect = true;
			}
			optionService.addOption(new Option(0, question, optionEmpty.get(index), isCorrect, 0, null));
		}
		
	}

	public void editQuestion(
		int questionID,
		String description,
		String difficulty,
		String hint,
		String type,
		int topicID,
		int authorID,
		List<String> indexFilled,
		List<String> optionFilled,
		String radioFilled
	) {
		
		List<Option> options = optionService.getAllOptionsByQuestionId(questionID);
		
		Question updatedQuestion = questionService.updateAndGetQuestion(
			questionID,
			new Question(
				questionID,
				description,
				QuestionDifficulty.valueOf(difficulty),
				personService.getSessionPerson(),
				hint.equals("") ? null: hint,
				new Topic(topicID, null, null),
				QuestionType.valueOf(type)
			)
		);
    
		if (!questionService.getQuestion(questionID).get().getType().name().equals(type)) {
			for (Option option: options) {
				optionService.deleteOption(option.getId());
			}

			for(int index = 0; index < optionFilled.size(); index++) {
				
				boolean isCorrect = false;
				if (index == Integer.parseInt(radioFilled)) {
					isCorrect = true;
				}
				optionService.addOption(new Option(0, updatedQuestion, optionFilled.get(index), isCorrect, 0, null));
			}
		} else {
			for (Option option : options) {
	    		for(int index = 0; index < indexFilled.size(); index++) {
	    			if (option.getId() == Integer.parseInt(indexFilled.get(index))) {
	    				option.setDescription(optionFilled.get(index));
	    				option.setCorrect(option.getId() == Integer.parseInt(radioFilled));
	    				optionService.updateOption(option.getId(), option);
	    				System.out.println("Option updated!");
	    			}
	    		}
	    	}
		}

    	for (String option : optionFilled) {
	    	System.out.println("option Open filled:" + option);
    	}
    	for (String index : indexFilled) {
	    	System.out.println("index Open filled:" + index);
    	}
	}
	
}
