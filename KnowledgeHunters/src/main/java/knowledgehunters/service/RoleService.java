package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Role;
import knowledgehunters.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired RoleRepository roleRepository;
	
	public List<Role> getAllRoles(){
		List<Role> roles = new ArrayList<>();
		roleRepository.findAll().forEach(roles ::add);
		return roles;
	}
	
	public Optional<Role> getRole(int id){
		
		return  roleRepository.findById(id);
	}

	public void addRole(Role role) {
		
		roleRepository.save(role);
	}
	public void updateRole(int id, Role role) {
		roleRepository.save(role);
		
	}
	public void deleteRole(int id) {
		
		roleRepository.deleteById(id);
		
	}
	
}
