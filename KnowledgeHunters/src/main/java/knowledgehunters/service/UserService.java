package knowledgehunters.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.User;
import knowledgehunters.repository.UserRepository;

@Service
public class UserService {
	@Autowired UserRepository userRepository;
	
	public List<User> getAllUsers(){
		List<User> users = new ArrayList<>();
		userRepository.findAll().forEach(users ::add);
		return users;
	}
	
	public Optional<User> getUser(int id){
		
		return  userRepository.findById(id);
	}

	public void addUser(User user) {
		
		userRepository.save(user);
	}
	public void updateUser(int id, User user) {
		userRepository.save(user);
		
	}
	public void deleteUser(int id) {
		
		userRepository.deleteById(id);
		
	}
	
	
}


