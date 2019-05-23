package knowledgehunters.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import knowledgehunters.model.Lesson;
import knowledgehunters.model.Person;
import knowledgehunters.model.Role;
import knowledgehunters.model.School;
import knowledgehunters.model.Topic;
import knowledgehunters.model.User;
import knowledgehunters.service.LessonService;
import knowledgehunters.service.PersonService;
import knowledgehunters.service.UserService;


@RestController
public class BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private PersonService personService;
	@Autowired
	private LessonService lessonService;
	
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
			
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) 
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
			response.sendRedirect("home"); 
		} else {
			//response.sendRedirect("errorPage");
			response.sendRedirect("login?loginError=true");
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
		
		response.sendRedirect("home"); 
	}

	@PostMapping("/login")
	public void saveRegistrationAndLogin(HttpServletResponse response, @RequestParam("username") String username, @RequestParam("displayName") String displayName, @RequestParam("email") String email, @RequestParam("school") int school, @RequestParam("password") String password, @RequestParam("isTeacher") boolean isTeacher) throws IOException {
			
		System.out.println("---Entered saveRegistrationAndLogin controller!");
		System.out.println(username + " | " + displayName + " | " + email + " | " + school + " | " + password);
		
		System.out.println("isTeacher: " + isTeacher);
		
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
	
	@PostMapping("/lessons/index")
	  public void LessonsIndex(HttpSession session, HttpServletResponse response, @RequestParam("id") String lessonID, @RequestParam("title") String title, @RequestParam("topic") String topicID, @RequestParam("description") String description) throws IOException {
		Person person = personService.getSessionPerson();
	    System.out.println("REST Lessons index controller post");
	    
	    lessonService.addLesson(new Lesson(lessonID.isEmpty()?0:Integer.parseInt(lessonID), title, new Topic(Integer.parseInt(topicID), null, null),person, description));
	    response.sendRedirect("/lessons/index");
	}
	
	@GetMapping("/lessons/delete/{id}")
	  public void LessonsDelete(HttpSession session, HttpServletResponse response, @PathVariable int id) throws IOException {
		
	    System.out.println("REST Lessons delete controller post");
	    
	    lessonService.deleteLesson(id);
	    response.sendRedirect("/lessons/index");
	}
	
	@PostMapping("/questions/index")
	  public void questionsIndex(
			  HttpSession session,
			  HttpServletResponse response,
			  @RequestParam("id") String questionID,
			  @RequestParam("title") String description,
			  @RequestParam("difficulty") String difficulty,
			  @RequestParam("author") String authorID,
			  @RequestParam("hint") String hint,
			  @RequestParam("topic") String topicID,
			  @RequestParam("type") String type,
			  @RequestParam(value = "radio-multiple-choice-filled", required=false) String multipleFilled,
			  @RequestParam(value = "radio-multiple-choice-empty", required=false) String multipleEmpty,
			  @RequestParam(value = "radio-true-false-filled", required=false) String trueFalseFilled,
			  @RequestParam(value = "radio-true-false-empty", required=false) String trueFalseEmpty,
			  @RequestParam(value = "radio-open-filled", required=false) String openFilled,
			  @RequestParam(value = "radio-open-empty", required=false) String openEmpty,
			  //@RequestParam(value = "option") String option
			  @RequestParam(value="option") List<String> option
		) throws IOException {
		
		Person person = personService.getSessionPerson();
	    System.out.println("REST Questions index controller post");
	    System.out.println(questionID + " | " + description + " | " + difficulty + " | " + authorID + " | " + hint + " | " + topicID + " | " + type);
	   
	    System.out.println("Option:" + option);
	    for (int i = 0; i< option.size(); i++) {
	    	System.out.println("Option " + i + ": " + option.get(i));
	    }

	    // clear all empty elements from option list
	    option.removeAll(Collections.singleton(""));
	    
	    for (int i = 0; i< option.size(); i++) {
	    	System.out.println("Option " + i + ": " + option.get(i));
	    }
	    
	    switch(type) {
	  		case "MULTIPLE_CHOICE": {
	  			System.out.println("Switch: multiple");
	  			System.out.println("Multiple filled:" + multipleFilled);
		    	System.out.println("Multiple empty:" + multipleEmpty);
	  			break;
	  		}
	  		case "TRUE_FALSE": {	
	  			System.out.println("Switch: true-false");
	  			System.out.println("True False filled:" + trueFalseFilled);
		    	System.out.println("True False empty:" + trueFalseEmpty);
	  			break;
	  		}
	  		case "OPEN": {
	  			System.out.println("Switch: open");
	  			System.out.println("Open filled:" + openFilled);
		    	System.out.println("Open empty:" + openEmpty);
	  			break;
	  		}
	  		case "MATCH": break;
	  		case "SORT": break;
	      }
	    /*
	    if (type.equals("MULTIPLE_CHOICE")) {
	    	System.out.println("Multiple filled:" + multipleFilled);
	    	System.out.println("Multiple empty:" + multipleEmpty);
	    }
	    */
//	    lessonService.addLesson(new Lesson(lessonID.isEmpty()?0:Integer.parseInt(lessonID), title, new Topic(Integer.parseInt(topicID), null, null),person, description));
	    response.sendRedirect("/questions/index");
	}
	
}
