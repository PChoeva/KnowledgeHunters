package knowledgehunters.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import knowledgehunters.service.PersonService;
import knowledgehunters.service.SubjectService;
import knowledgehunters.service.UserService;

@Controller
@SpringBootApplication
public class MainController {
	
	@Autowired
	UserService userService;
	@Autowired
	PersonService personService;
	@Autowired
	SubjectService subjectService;
	
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
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("view", "user/register");
		return "start-layout";
	}
	
	@GetMapping("/home")
	public String home(Model model) {
		model.addAttribute("view", "user/home");
		System.out.println("---Entered home controller!");
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
		return "base-layout";
	}
	

}
