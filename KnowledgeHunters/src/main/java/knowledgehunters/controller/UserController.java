package knowledgehunters.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import knowledgehunters.model.User;
import knowledgehunters.service.UserService;


@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping ("/users") 
	public List<User> getAllUsers() {
		return  userService.getAllUsers();
	}
	
	@GetMapping ("/users/{id}") 
	public Optional<User> getUserId(@PathVariable String id) {	
		return  userService.getUser(id);
	}
	
	@PostMapping(value="/users")
	public void addUser(@RequestBody User user){
		
		userService.addUser(user);	
	}
	
	@PutMapping(value ="/users/{id}")
	public void updateUser(@RequestBody User user,@PathVariable String id) {
		userService.updateUser(id, user);
	}
	
	@DeleteMapping(value ="/users/{id}")
	public void deleteUser(@PathVariable String id) {
		userService.deleteUser(id);
	}
}
