package knowledgehunters.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

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
import knowledgehunters.model.Lesson;
import knowledgehunters.model.Option;
import knowledgehunters.model.Person;
import knowledgehunters.model.School;
import knowledgehunters.service.LessonService;
import knowledgehunters.service.OptionService;
import knowledgehunters.service.PersonService;
import knowledgehunters.service.QuestionService;
import knowledgehunters.service.RoleService;
import knowledgehunters.service.SubjectService;
import knowledgehunters.service.TopicService;
import knowledgehunters.service.UserService;
import knowledgehunters.service.SchoolService;

@Controller
@SpringBootApplication
public class MainController {
	
	@Autowired
	UserService userService;
	@Autowired
	PersonService personService;
	@Autowired
	SubjectService subjectService;
	@Autowired
	SchoolService schoolService;
	@Autowired
	TopicService topicService;
	@Autowired
	LessonService lessonService;
	@Autowired
	RoleService roleService;
	@Autowired
	QuestionService questionService;
	@Autowired
	OptionService optionService;
	
	private static String ADMIN = "Admin";
	private static String TEACHER = "Teacher";
	private static String STUDENT = "Student";
	
	
	@GetMapping(value= {"/", "/index"})
	public String index(Model model) {
		model.addAttribute("view", "index");
		return "start-layout";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
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
	public String register(Model model) {
		model.addAttribute("view", "user/register");
		model.addAttribute("schools", schoolService.getAllSchools());
		List<School> schools = schoolService.getAllSchools();
		schools.forEach(s -> System.out.println(s.getName()));
		return "start-layout";
	}
	
	@GetMapping("/home")
	public String home(Model model) {
		if (isLogged(model)) return "start-layout"; 

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
	
	@GetMapping("/lessons/index")
	public String lessonIndex(HttpSession session, Model model) {
		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER)))) {
			return "base-layout";
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
	    response.sendRedirect("/lessons/index");
	}	
	
	@GetMapping("/questions/index")
	public String questionIndex(HttpSession session, Model model) {
System.out.println("---------in questionIndex controller");

		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN, TEACHER)))) {
			return "base-layout";
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
	
	@GetMapping("/questions/delete/{id}")
	public String questionDelete(Model model, @PathVariable int id) {

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
		
		return "redirect:/questions/index";
	}
	
	@GetMapping("/userslist")
	public String userslist(Model model) {

		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN)))) {
			return "base-layout";
		}

		model.addAttribute("view", "user/userslist");
		model.addAttribute("users", userService.getAllUsers());
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
	public String teachersApprove(Model model, @PathVariable int id) {

		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN)))) {
			return "base-layout";
		}

		
		System.out.println("teachersApprove->id: " + id);
		Person teacher = personService.getPerson(id).get();
		teacher.setIsApproved(true);
		
		personService.updatePerson(id, teacher);
		System.out.println("teachersApprove->getApproved: " + personService.getPerson(id).get().getIsApproved());
		
		return "redirect:/teachers-for-approval";
	}
	
	@GetMapping("/teachers-for-approval/deny/{id}")
	public String teachersDeny(Model model, @PathVariable int id) {

		if (isLogged(model)) return "start-layout"; 
		if (!hasRights(model, new ArrayList<>(Arrays.asList(ADMIN)))) {
			return "base-layout";
		}

		System.out.println("teachersApprove->id: " + id);		
		personService.deletePerson(id);
		
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
			model.addAttribute("errorMsg", "Трябва да се регистрирате за тази страница!");
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
			model.addAttribute("errorMsg", "Нямате права за тази страница!");
			// replace with setView ??????
		} 
		return hasPermission;
	}
		
	public void setView(Model model) {
		model.addAttribute("view", "/error");
		model.addAttribute("errorCode", "403");
		model.addAttribute("errorMsg", "Нямате права за тази страница!");
	}
}
