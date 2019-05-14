package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;

import knowledgehunters.model.Role;
import knowledgehunters.model.User;

public class Service {
	List<User> users = new ArrayList<User>();
	
	public List<User> getUsersFromArray(){
//		Role admin = new Role(1,"Admin");
//		User user = new User(1, "user1", "pass1", admin);
//		users.add(user);	
		
		return users;
	}
	
	public User getUserById(int id){
		List<User> users = getUsersFromArray();
		for(User user : users) {
//			if(user.getId() == id) return user;
		}
		
		return null;
	}
	

}
