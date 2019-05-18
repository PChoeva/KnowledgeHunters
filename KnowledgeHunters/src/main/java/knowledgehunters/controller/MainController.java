package knowledgehunters.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import knowledgehunters.model.Person;
import knowledgehunters.model.School;
import knowledgehunters.service.LessonService;
import knowledgehunters.service.PersonService;
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
