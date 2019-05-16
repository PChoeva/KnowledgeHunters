package knowledgehunters.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import knowledgehunters.model.Person;
import knowledgehunters.model.Role;
import knowledgehunters.model.School;
import knowledgehunters.model.User;
import knowledgehunters.service.PersonService;
import knowledgehunters.service.UserService;


@RestController
public class BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private PersonService personService;
	
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
			
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) 
			{
				System.out.println("in if for users");
				session.setAttribute("sessionUsername", username);
		        session.setAttribute("sessionPassword", password);

		        isUserInDB = true;
//			    response.sendRedirect("home");
			}
			
		}

		if (isUserInDB) {
			response.sendRedirect("home"); 
		} else {
			//response.sendRedirect("errorPage");
			response.sendRedirect("login?loginError=true");
		}
	}
	

	@PostMapping("/login")
	public void saveRegistrationAndLogin(HttpServletResponse response, @RequestParam("username") String username, @RequestParam("displayName") String displayName, @RequestParam("email") String email, @RequestParam("school") int school, @RequestParam("password") String password) throws IOException {
			
		System.out.println("---Entered saveRegistrationAndLogin controller!");
		
		System.out.println(username + " | " + displayName + " | " + email + " | " + school + " | " + password);
		
		userService.addUser(new User(0, username, password, new Role(3,null)));
		personService.addPerson(new Person(0,userService.findUser(username), new School(school,null, null), displayName, email));
		response.sendRedirect("login"); 
	}
	

	@GetMapping("/logout")
	  public void logout(HttpSession session, HttpServletResponse response) throws IOException {
	    session.invalidate();
	    System.out.println("Logout controller post");
	    response.sendRedirect("login");
	}
	
}
