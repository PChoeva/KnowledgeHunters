package knowledgehunters.controller;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

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
		model.addAttribute("view", "user/home");
		System.out.println("---Entered home controller!");
		return "base-layout";
	}
	
	@GetMapping("/profile")
	public String profile(HttpSession session, Model model) {
		model.addAttribute("schools", schoolService.getAllSchools());
		
//		session.getAttribute("sessionUserID");
//		System.out.println("profileController - userID:" + session.getAttribute("userID"));
		
		Person person = personService.getSessionPerson();
		model.addAttribute("person", person);
		
//		personService.getPerson(2).ifPresent(p -> model.addAttribute("person", p));
//		model.addAttribute("person", personService.getPerson(1));
		model.addAttribute("view", "user/profile");
		return "base-layout";
	}
	
	@GetMapping("/lessons/index")
	public String lessonIndex(HttpSession session, Model model) {
System.out.println("---------in lessonIndex controller");
//		Lesson lesson = personService.getSessionPerson();
		Person person = personService.getSessionPerson();
		System.out.println("person: " + person.getDisplayName());
		System.out.println(lessonService.getAllLessonsByAuthor(person));
//		model.addAttribute("personID", person.getId());
		model.addAttribute("lessons", lessonService.getAllLessonsByAuthor(person));
		lessonService.getAllLessonsByAuthor(person).forEach(l -> System.out.println("Lesson:" + l));
//		model.addAttribute("topics", topicService.getAllTopics());
//		lessonService.getLesson(1).ifPresent(lesson -> model.addAttribute("lesson", lesson));
//		model.addAttribute("lesson", lesson);
		
		model.addAttribute("view", "lesson/index");
		return "base-layout";
	}
	
	@GetMapping("/lessons/add")
	public String lessonAdd(HttpSession session, Model model) {
		model.addAttribute("schools", schoolService.getAllSchools());
		
//		Lesson lesson = personService.getSessionPerson();
		
		
		model.addAttribute("lesson", null);
		model.addAttribute("sectionTitle", "Добави урок");
		model.addAttribute("topics", topicService.getAllTopics());
//		lessonService.getLesson(1).ifPresent(lesson -> model.addAttribute("lesson", lesson));
//		model.addAttribute("lesson", lesson);
		
		model.addAttribute("view", "lesson/form");
		return "base-layout";
	}
	
	@GetMapping("/lessons/edit/{id}")
	public String lessonEdit(HttpSession session, Model model, @PathVariable int id) {
		model.addAttribute("schools", schoolService.getAllSchools());
		System.out.println("lessonEdit->id: " + id);
//		Lesson lesson = personService.getSessionPerson();
		
		lessonService.getLesson(id).ifPresent(lesson -> model.addAttribute("lesson", lesson));
		
		model.addAttribute("sectionTitle", "Промени урок");
		model.addAttribute("topics", topicService.getAllTopics());
//		lessonService.getLesson(1).ifPresent(lesson -> model.addAttribute("lesson", lesson));
//		model.addAttribute("lesson", lesson);
		
		model.addAttribute("view", "lesson/form");
		return "base-layout";
	}
	
	@GetMapping("/questions/index")
	public String questionIndex(HttpSession session, Model model) {
System.out.println("---------in questionIndex controller");
		Person person = personService.getSessionPerson();
		System.out.println("person: " + person.getDisplayName());
		System.out.println(questionService.getAllQuestionsByAuthor(person));
//		model.addAttribute("personID", person.getId());
		model.addAttribute("questions", questionService.getAllQuestionsByAuthor(person));
		questionService.getAllQuestionsByAuthor(person).forEach(q -> System.out.println("Question:" + q));
		
		model.addAttribute("view", "question/index");
		return "base-layout";
	}
	
	@GetMapping("/questions/edit/{id}")
	public String questionEdit(HttpSession session, Model model, @PathVariable int id) {
//		model.addAttribute("schools", schoolService.getAllSchools());
		System.out.println("questionEdit->id: " + id);
//		Lesson lesson = personService.getSessionPerson();
		
		questionService.getQuestion(id).ifPresent(question -> model.addAttribute("question", question));
		
		model.addAttribute("sectionTitle", "Промени въпрос");
		model.addAttribute("topics", topicService.getAllTopics());
		
		List<QuestionDifficulty> difficulties = new ArrayList<QuestionDifficulty>(EnumSet.allOf(QuestionDifficulty.class));
		model.addAttribute("difficulties", difficulties);
		difficulties.forEach(d -> System.out.println("Difficulty: " + d));
		
		List<QuestionType> types = new ArrayList<QuestionType>(EnumSet.allOf(QuestionType.class));
		model.addAttribute("types", types);
		types.forEach(t -> System.out.println("Type: " + t));
		
		
		
		model.addAttribute("options",optionService.getAllOptionsByQuestionId(id));
		optionService.getAllOptionsByQuestionId(id).forEach(o -> System.out.println("Option is true?: " + o.isCorrect()));
		
//		lessonService.getLesson(1).ifPresent(lesson -> model.addAttribute("lesson", lesson));
//		model.addAttribute("lesson", lesson);
		
		model.addAttribute("view", "question/form");
		return "base-layout";
	}
	
	
	@GetMapping("/userslist")
	public String userslist(Model model) {
		model.addAttribute("view", "user/userslist");
		model.addAttribute("users", userService.getAllUsers());
		return "base-layout";
	}
	
	@GetMapping("/gain-knowledge")
	public String gainKnowledge(Model model) {
		model.addAttribute("view", "user/gain-knowledge");
		model.addAttribute("subjects", subjectService.getAllSubjects());
		model.addAttribute("lessons", lessonService.getAllLessons());
		return "base-layout";
	}
	
	@GetMapping("/errorPage")
	public String error(Model model) {
		model.addAttribute("view", "errorPage");
		System.out.println("---Entered error controller!");
		return "base-layout";
	}
}
