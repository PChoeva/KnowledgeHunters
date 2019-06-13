package knowledgehunters.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import knowledgehunters.enums.QuestionDifficulty;
import knowledgehunters.enums.QuestionType;
import knowledgehunters.model.GameMove;
import knowledgehunters.model.Lesson;
import knowledgehunters.model.Option;
import knowledgehunters.model.Person;
import knowledgehunters.model.Question;
import knowledgehunters.model.Ranking;
import knowledgehunters.model.School;
import knowledgehunters.model.Student;
import knowledgehunters.model.Subject;
import knowledgehunters.repository.StudentRepository;
import knowledgehunters.service.GameMoveService;
import knowledgehunters.service.GameService;
import knowledgehunters.service.LessonService;
import knowledgehunters.service.OptionService;
import knowledgehunters.service.PersonService;
import knowledgehunters.service.QuestionService;
import knowledgehunters.service.RankingService;
import knowledgehunters.service.RoleService;
import knowledgehunters.service.SubjectService;
import knowledgehunters.service.TopicService;
import knowledgehunters.service.UserService;
import knowledgehunters.service.SchoolService;
import knowledgehunters.service.StudentService;

@Controller
@SpringBootApplication
public class MainController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private PersonService personService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private LessonService lessonService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private OptionService optionService;
	@Autowired
	private GameService gameService;
	@Autowired
	private GameMoveService gameMoveService;
	@Autowired
	private RankingService rankingService;
	
	@Autowired
	StudentRepository studentRepository;
	
	private static String ADMIN = "Admin";
	private static String TEACHER = "Teacher";
	private static String STUDENT = "Student";
	private static int NUMBER_OF_QUESTIONS_PER_GAME = 5;
	
	
	@GetMapping(value= {"/", "/index"})
	public String index(Model model) {
		model.addAttribute("view", "index");
		return "start-layout";
	}
	
	@GetMapping("/login")
	public String login(HttpSession session, Model model) {
		if (session.getAttribute("successMsg") != null) {
			model.addAttribute("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		if (session.getAttribute("errorMsg") != null) {
			model.addAttribute("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		model.addAttribute("view", "user/login");
		return "start-layout";
	}
	
	@GetMapping("/logout")
	  public void logout(HttpSession session, HttpServletResponse response) throws IOException {
	    session.invalidate();
	    personService.deleteSessionPerson();
	    System.out.println("Logout controller post");
	    response.sendRedirect("login");
	}
	
	/*NOT SURE IF IT WORKS*/
	@GetMapping("/login?loginError=true")
	public String loginErr(Model model) {
		model.addAttribute("view", "user/login");
		model.addAttribute("loginError", "true");
		return "start-layout";
	}
	
	@GetMapping("/register")
	public String register(HttpSession session, Model model) {
		if (session.getAttribute("errorMsg") != null) {
			model.addAttribute("errorMsg", session.getAttribute("errorMsg"));
			session.removeAttribute("errorMsg");
		}
		
		model.addAttribute("view", "user/register");
		model.addAttribute("schools", schoolService.getAllSchools());
		List<School> schools = schoolService.getAllSchools();
		schools.forEach(s -> System.out.println(s.getName()));
		return "start-layout";
	}
	
	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
		if (isLogged(model)) return "start-layout"; 

		System.out.println("successMsg: " + session.getAttribute("successMsg"));
		if (session.getAttribute("successMsg") != null) {
			System.out.println("successMsg: " + session.getAttribute("successMsg"));
			model.addAttribute("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		model.addAttribute("view", "user/home");
		System.out.println("---Entered home controller!");
		return "base-layout";
	}
	
	@GetMapping("/profile")
	public String profile(HttpSession session, Model model) {
		if (isLogged(model)) return "start-layout"; 

		model.addAttribute("schools", schoolService.getAllSchools());

		Person person = personService.getSessionPerson();
		model.addAttribute("person", person);
		
		model.addAttribute("view", "user/profile");
		return "base-layout";
	}
	
	@GetMapping("/user/change-password")
	public String changePassword(HttpSession session, Model model) {
		if (isLogged(model)) return "start-layout"; 

		Person person = personService.getSessionPerson();
		model.addAttribute("person", person);
		
		model.addAttribute("view", "user/change-password");
		return "base-layout";
	}
	
	@GetMapping("/lessons/index")
	public String lessonIndex(HttpSession session, Model model) {
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER)))) {
			return "base-layout";
		}

		if (session.getAttribute("successMsg") != null) {
			model.addAttribute("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		
		Person person = personService.getSessionPerson();
		model.addAttribute("lessons", lessonService.getAllLessonsByAuthor(person));
		lessonService.getAllLessonsByAuthor(person).forEach(l -> System.out.println("Lesson:" + l));
		
		model.addAttribute("view", "lesson/index");
		return "base-layout";
	}
	
	@GetMapping("/lessons/list/{id}")
	public String lessonList(HttpSession session, Model model, @PathVariable int id) {
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER, STUDENT)))) {
			return "base-layout";
		}

		
		model.addAttribute("lessons", lessonService.getAllLessonsBySubjectId(id));
		lessonService.getAllLessonsBySubjectId(id).forEach(l -> System.out.println("Lesson:" + l));
		
		model.addAttribute("subject", subjectService.getSubject(id).get().getName());
		model.addAttribute("view", "lesson/list");
		return "base-layout";
	}
	
	@GetMapping("/lessons/add")
	public String lessonAdd(HttpSession session, Model model) {
		
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER)))) {
			return "base-layout";
		}

		model.addAttribute("schools", schoolService.getAllSchools());
				
		model.addAttribute("lesson", null);
		model.addAttribute("sectionTitle", "Добави урок");
		model.addAttribute("topics", topicService.getAllTopics());
		
		model.addAttribute("view", "lesson/form");
		return "base-layout";
	}
	
	@GetMapping("/lessons/edit/{id}")
	public String lessonEdit(HttpSession session, Model model, @PathVariable int id) {
		
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER)))) {
			return "base-layout";
		}
		
		model.addAttribute("schools", schoolService.getAllSchools());
		System.out.println("lessonEdit->id: " + id);
		
		lessonService.getLesson(id).ifPresent(lesson -> model.addAttribute("lesson", lesson));
		
		model.addAttribute("sectionTitle", "Промени урок");
		model.addAttribute("topics", topicService.getAllTopics());
		
		model.addAttribute("view", "lesson/form");
		return "base-layout";
	}
	
	@GetMapping("/lessons/view/{id}")
	public String lessonView(HttpSession session, Model model, @PathVariable int id) {
		
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER)))) {
			return "base-layout";
		}
		
		lessonService.getLesson(id).ifPresent(lesson -> model.addAttribute("lesson", lesson));
		lessonService.getLesson(id).ifPresent(lesson -> System.out.println("View lesson: " + lesson.getTitle()));
		
		model.addAttribute("sectionTitle", "Урок");		
		model.addAttribute("view", "lesson/view");
		return "base-layout";
	}
	
	@GetMapping("/lessons/delete/{id}")
	  public void LessonsDelete(HttpSession session, HttpServletResponse response, @PathVariable int id) throws IOException {
		
	    System.out.println("REST Lessons delete controller post");
	    lessonService.deleteLesson(id);
	    session.setAttribute("successMsg", "Успешно изтриване!");
	    response.sendRedirect("/lessons/index");
	}	
	
	@GetMapping("/questions/index")
	public String questionIndex(HttpSession session, Model model) {
System.out.println("---------in questionIndex controller");

		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER)))) {
			return "base-layout";
		}

		if (session.getAttribute("successMsg") != null) {
			model.addAttribute("successMsg", session.getAttribute("successMsg"));
			session.removeAttribute("successMsg");
		}
		
		Person person = personService.getSessionPerson();
		model.addAttribute("questions", questionService.getAllQuestionsByAuthor(person));
		questionService.getAllQuestionsByAuthor(person).forEach(q -> System.out.println("Question:" + q));
		
		model.addAttribute("view", "question/index");
		return "base-layout";
	}
	
	@GetMapping("/questions/edit/{id}")
	public String questionEdit(HttpSession session, Model model, @PathVariable int id) {
		
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER)))) {
			return "base-layout";
		}
		
		System.out.println("questionEdit->id: " + id);
		
		questionService.getQuestion(id).ifPresent(question -> model.addAttribute("question", question));
		
		model.addAttribute("sectionTitle", "Промени въпрос");
		model.addAttribute("topics", topicService.getAllTopics());
		
		List<QuestionDifficulty> difficulties = new ArrayList<QuestionDifficulty>(EnumSet.allOf(QuestionDifficulty.class));
		model.addAttribute("difficulties", difficulties);
		difficulties.forEach(d -> System.out.println("Difficulty: " + d));
		
		List<QuestionType> types = new ArrayList<QuestionType>(EnumSet.allOf(QuestionType.class));
		model.addAttribute("types", types);
		types.forEach(t -> System.out.println("Type: " + t));
		
		
		model.addAttribute("currPerson", personService.getSessionPerson());
		model.addAttribute("options",optionService.getAllOptionsByQuestionId(id));
		optionService.getAllOptionsByQuestionId(id).forEach(o -> System.out.println("Option is true?: " + o.isCorrect()));
		
		model.addAttribute("view", "question/form");
		return "base-layout";
	}
	
	@GetMapping("/questions/add")
	public String questionAdd(HttpSession session, Model model) {
		
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER)))) {
			return "base-layout";
		}
		
		model.addAttribute("sectionTitle", "Добави въпрос");
		model.addAttribute("topics", topicService.getAllTopics());
		
		List<QuestionDifficulty> difficulties = new ArrayList<QuestionDifficulty>(EnumSet.allOf(QuestionDifficulty.class));
		model.addAttribute("difficulties", difficulties);
		difficulties.forEach(d -> System.out.println("Difficulty: " + d));
		
		List<QuestionType> types = new ArrayList<QuestionType>(EnumSet.allOf(QuestionType.class));
		model.addAttribute("types", types);
		types.forEach(t -> System.out.println("Type: " + t));
		
		model.addAttribute("currPerson", personService.getSessionPerson());
		model.addAttribute("view", "question/form");
		return "base-layout";
	}
	
	@GetMapping("/questions/view/{id}")
	public String questionView(HttpSession session, Model model, @PathVariable int id) {
		
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER)))) {
			return "base-layout";
		}
		
		questionService.getQuestion(id).ifPresent(question -> model.addAttribute("question", question));
		questionService.getQuestion(id).ifPresent(question -> System.out.println("View question: " + question.getDescription()));
		
		List<Option> questionOptions = optionService.getAllOptionsByQuestionId(id);
		questionOptions.forEach(o -> System.out.println("OPTION: " + o.getDescription()));
		model.addAttribute("options", questionOptions);
		
		model.addAttribute("sectionTitle", "Въпрос");		
		model.addAttribute("view", "question/view");
		return "base-layout";
	}
	
	@GetMapping("/questions/delete/{id}")
	public String questionDelete(HttpSession session, Model model, @PathVariable int id) {

		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER)))) {
			return "base-layout";
		}

		System.out.println("questionDelete->id: " + id);
		
		for (Option option : optionService.getAllOptionsByQuestionId(id)) {
			System.out.println("Delete option: " + option.getDescription());
			optionService.deleteOption(option.getId());
		}
		questionService.deleteQuestion(id);
		session.setAttribute("successMsg", "Успешно изтриване!");		
		return "redirect:/questions/index";
	}
	
	@GetMapping("/userslist")
	public String userslist(Model model) {

		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN)))) {
			return "base-layout";
		}

		model.addAttribute("view", "user/userslist");
		model.addAttribute("people", personService.getAllPeople());
		return "base-layout";
	}
	
	@GetMapping("/teachers-for-approval")
	public String teachersForApproval(Model model) {

		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN)))) {
			return "base-layout";
		}

		List<Person> teachersForApproval = new ArrayList<Person>();
		for (Person person : personService.getAllPeople()) {
			if(!person.getIsApproved()) {
				teachersForApproval.add(person);
			}
		}
		
		model.addAttribute("view", "user/teachers-for-approval");
		model.addAttribute("teachers", teachersForApproval);
		return "base-layout";
	}
	
	
	@GetMapping("/teachers-for-approval/approve/{id}")
	public String teachersApprove(HttpSession session, Model model, @PathVariable int id) {

		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN)))) {
			return "base-layout";
		}

		
		System.out.println("teachersApprove->id: " + id);
		Person teacher = personService.getPerson(id).get();
		teacher.setIsApproved(true);
		
		personService.updatePerson(id, teacher);
		System.out.println("teachersApprove->getApproved: " + personService.getPerson(id).get().getIsApproved());
		session.setAttribute("successMsg", "Успешно одобряване!");
		return "redirect:/teachers-for-approval";
	}
	
	@GetMapping("/teachers-for-approval/deny/{id}")
	public String teachersDeny(HttpSession session, Model model, @PathVariable int id) {

		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN)))) {
			return "base-layout";
		}

		System.out.println("teachersApprove->id: " + id);		
		personService.deletePerson(id);
		session.setAttribute("successMsg", "Успешно изтриване!");
		return "redirect:/teachers-for-approval";
	}
	
	@GetMapping("/gain-knowledge")
	public String gainKnowledge(Model model) {
		
		if (isLogged(model)) return "start-layout"; 

		model.addAttribute("view", "user/gain-knowledge");
		model.addAttribute("subjects", subjectService.getAllSubjects());
		model.addAttribute("lessons", lessonService.getAllLessons());
		return "base-layout";
	}
	
	@GetMapping("/game/filters")
	public String gameFilters(HttpSession session, Model model) {
		
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER, STUDENT)))) {
			return "base-layout";
		}
		
		List<QuestionDifficulty> questionDifficulties = Arrays.asList(QuestionDifficulty.values());
		System.out.println("====Question difficulties====");
		questionDifficulties.forEach(d -> System.out.println(d.getValue()));
		model.addAttribute("difficulties", questionDifficulties);
		
		
		List<Question> questions = questionService.getAllQuestions();
		List<Subject> subjects = subjectService.getAllSubjects();
		
		Map<Integer, Integer> subjectIDsWithNumberOfQuestions = new HashMap<Integer, Integer>();
		
		for (Subject subject: subjects) {
			subjectIDsWithNumberOfQuestions.put(subject.getId(), 0);
		}
		
		for (Question question : questions) {
			int currNumberOfQuestions = subjectIDsWithNumberOfQuestions.get(question.getTopic().getSubject().getId());
			subjectIDsWithNumberOfQuestions.put(question.getTopic().getSubject().getId(), currNumberOfQuestions + 1);
		}
		
		subjectIDsWithNumberOfQuestions.entrySet().forEach(entry->{
			    System.out.println(entry.getKey() + " " + entry.getValue());  
		});
		
		
		List<Subject> subjectsWith5orMoreQuestions = new ArrayList<>();
		subjectIDsWithNumberOfQuestions.entrySet().forEach(entry->{
		    if (entry.getValue() >= NUMBER_OF_QUESTIONS_PER_GAME) {
		    	subjectService.getSubject(entry.getKey()).ifPresent(subject ->subjectsWith5orMoreQuestions.add(subject));		    	
		    }
		});
		System.out.println("====subjectsWith5orMoreQuestions====");
		subjectsWith5orMoreQuestions.forEach(s -> System.out.println(s.getId() + " | " + s.getName()));
		
		model.addAttribute("subjects", subjectsWith5orMoreQuestions);
		model.addAttribute("sectionTitle", "Избери");		
		model.addAttribute("view", "game/filters");
		return "base-layout";
	}
	
	
	@GetMapping("/game/filters/subject/{id}")
	public String gameFiltersSubject(HttpSession session, Model model, @PathVariable int id) {
		
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER, STUDENT)))) {
			return "base-layout";
		}
		
		
		//Set<QuestionDifficulty> questionDifficultiesWithQuestions = new HashSet<>();
		
		HashMap<QuestionDifficulty, Integer> questionDifficultiesCountQuestions = new HashMap<QuestionDifficulty, Integer>();
		questionDifficultiesCountQuestions.put(QuestionDifficulty.EASY, 0);
		questionDifficultiesCountQuestions.put(QuestionDifficulty.MEDIUM, 0);
		questionDifficultiesCountQuestions.put(QuestionDifficulty.HARD, 0);
		List<Question> questions = questionService.getAllQuestions();
		for (Question question :questions) {
			if (question.getTopic().getSubject().getId() == id) {
				questionDifficultiesCountQuestions.put(question.getDifficulty(), questionDifficultiesCountQuestions.get(question.getDifficulty()) + 1);
			}
		}
		Set<QuestionDifficulty> difficultiesWithNQuestions =  new HashSet<>();
		
//		for (int i = 0; i < NUMBER_OF_QUESTIONS_PER_GAME ; i++) {
//			questionDifficultiesCountQuestions.values().remove(i);
//		}
		
		for (Map.Entry<QuestionDifficulty, Integer> entry : questionDifficultiesCountQuestions.entrySet()) {
		    if (entry.getValue() >= NUMBER_OF_QUESTIONS_PER_GAME) {
		    	difficultiesWithNQuestions.add(entry.getKey());
		    }
		}
		
		
		model.addAttribute("difficulties", difficultiesWithNQuestions);
		return "ajax-layout";
		/*
		List<QuestionDifficulty> questionDifficulties = Arrays.asList(QuestionDifficulty.values());
		System.out.println("====Question difficulties====");
		questionDifficulties.forEach(d -> System.out.println(d.getValue()));
		model.addAttribute("difficulties", questionDifficulties);
		
		List<Question> questions = questionService.getAllQuestions();
		List<Subject> subjects = subjectService.getAllSubjects();
		
		Map<Integer, Integer> subjectIDsWithNumberOfQuestions = new HashMap<Integer, Integer>();
		
		for (Subject subject: subjects) {
			subjectIDsWithNumberOfQuestions.put(subject.getId(), 0);
		}
		
		for (Question question : questions) {
			int currNumberOfQuestions = subjectIDsWithNumberOfQuestions.get(question.getTopic().getSubject().getId());
			subjectIDsWithNumberOfQuestions.put(question.getTopic().getSubject().getId(), currNumberOfQuestions + 1);
		}
		
		subjectIDsWithNumberOfQuestions.entrySet().forEach(entry->{
			    System.out.println(entry.getKey() + " " + entry.getValue());  
		});
		
		
		List<Subject> subjectsWith5orMoreQuestions = new ArrayList<>();
		subjectIDsWithNumberOfQuestions.entrySet().forEach(entry->{
		    if (entry.getValue() >= NUMBER_OF_QUESTIONS_PER_GAME) {
		    	subjectService.getSubject(entry.getKey()).ifPresent(subject ->subjectsWith5orMoreQuestions.add(subject));		    	
		    }
		});
		System.out.println("====subjectsWith5orMoreQuestions====");
		subjectsWith5orMoreQuestions.forEach(s -> System.out.println(s.getId() + " | " + s.getName()));
		
		model.addAttribute("subjects", subjectsWith5orMoreQuestions);
		*/
	}
	
	@GetMapping("/game-move")
	public String gameMove(HttpSession session, Model model) {
		
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER, STUDENT)))) {
			return "base-layout";
		}
		
		String difficulty = (String) session.getAttribute("gameDifficulty");
		String subjectId = (String) session.getAttribute("gameSubject");
		
		QuestionDifficulty diff = QuestionDifficulty.valueOf(difficulty);
		
		List<Question> filteredQuestions = questionService.getQuestionsByDifficultyAndSubjectId(diff, Integer.parseInt(subjectId));
		System.out.println("====GET FILTERED QUESTIONS====");
		filteredQuestions.forEach(q -> System.out.println("Filtered Q:" + q.getDescription()));
		List<Question> firstNQuestions = filteredQuestions.stream().limit(NUMBER_OF_QUESTIONS_PER_GAME).collect(Collectors.toList());
		
		firstNQuestions.forEach(q -> System.out.println("First N Q:" + q.getDescription()));

		model.addAttribute("questions", firstNQuestions);
		model.addAttribute("optionService", optionService);
		model.addAttribute("typeOpen", QuestionType.OPEN);
		String subjectName = subjectService.getSubject(Integer.parseInt(subjectId)).get().getName();
		model.addAttribute("gameSubject", "Тема - " + subjectName);		
		model.addAttribute("gameDifficulty", "Сложност - " + diff.getValue());
		model.addAttribute("view", "game/game-move");
		return "base-layout";
	}
	
	@GetMapping("/game/review")
	public String gameReview(HttpSession session, Model model) {
		
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER, STUDENT)))) {
			return "base-layout";
		}
		// smqtame tochkite i sravnqvame otgovori
		
		//vypros, daden otg(tikche/X), veren otg(samo ako negoviq e greshen)  --> v tablica
		int gameId = (int) session.getAttribute("gameId");
		int gamePoints = (int) session.getAttribute("gamePoints");
		List<GameMove> gameMoves = gameMoveService.getGameMovesByGameId(gameId);
		
		model.addAttribute("gameMoves", gameMoves);
		model.addAttribute("gamePoints", gamePoints);
		model.addAttribute("optionService", optionService);
		model.addAttribute("view", "game/review");
		return "base-layout";
	}
	
	@GetMapping("/ranking/subject/{id}")
	public String rankingBySubject(HttpSession session, Model model, @PathVariable int id) {

		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER, STUDENT)))) {
			return "base-layout";
		}
		
		List<Student> students;
		if (id == 0) students = studentService.getAllStudents();
		else {
			students = new ArrayList<>();
			List<Ranking> rankings = rankingService.findAllRankingsBySubjectId(id);
			if (rankings != null) {
				for (Ranking ranking :rankings) {
					ranking.getStudent().setPoints(ranking.getPoints());
					students.add(ranking.getStudent());
				}
				students.forEach(s -> System.out.println("Student: " + s.getPerson().getDisplayName() + " | points: " + s.getPoints()));
			}
			
		}
		
		
		
		Collections.sort(students, new Comparator<Student>(){
		    public int compare(Student s1, Student s2) {
		    	if (s1.getPoints() > s2.getPoints()) return -1;
		    	if (s1.getPoints() < s2.getPoints()) return 1;
		        return 0;
		    }
		});

		model.addAttribute("students", students);
		model.addAttribute("currSubject", id);
		model.addAttribute("subjects", subjectService.getAllSubjects());
		model.addAttribute("view", "game/ranking");
		return "base-layout";
	}
	
	
	@GetMapping("/error")
	public String error(Model model) {
		if (isLogged(model)) return "start-layout"; 

		model.addAttribute("view", "error");
		System.out.println("---Entered error controller!");
		return "base-layout";
	}
	
	public Boolean isLogged(Model model) {
		if (personService.getSessionPerson() == null) {
			model.addAttribute("view", "/error");
			model.addAttribute("errorCode", "403");
			model.addAttribute("errorMessage", "Трябва да се регистрирате за тази страница!");
			return true;
		}
		return false;
	}
	
	public Boolean hasRights(Model model, List<String> roles) {
		System.out.println("hasRights Has: " + personService.getSessionPerson().getUser().getRole().getName());
		
		roles.forEach(r -> System.out.println("hasRights Given: " + r));
		Boolean hasPermission = false;
		for (String role : roles) {
			if (personService.getSessionPerson().getUser().getRole().getName().equals(role)) {
				hasPermission = true;
			}
		}
		
		
		if (!hasPermission) {
			model.addAttribute("view", "/error");
			model.addAttribute("errorCode", "403");
			model.addAttribute("errorMessage", "Нямате права за тази страница!");
			// replace with setView ??????
		} 
		return hasPermission;
	}
		
	public void setView(Model model) {
		model.addAttribute("view", "/error");
		model.addAttribute("errorCode", "403");
		model.addAttribute("errorMessage", "Нямате права за тази страница!");
	}
}
