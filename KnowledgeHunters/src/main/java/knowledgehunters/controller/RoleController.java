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

import knowledgehunters.model.Role;
import knowledgehunters.service.RoleService;

@RestController
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@GetMapping ("/roles") 
	public List<Role> getAllRoles() {
		return  roleService.getAllRoles();
	}
	@GetMapping ("/roles/{id}") 
	public Optional<Role> getRoleId(@PathVariable String id) {
		
		
		return  roleService.getRole(id);
	}
	@PostMapping(value="/roles")
	public void addRole(@RequestBody Role role){
		
		roleService.addRole(role);	
	}
	@PutMapping(value ="/roles/{id}")
	public void updateRole(@RequestBody Role role,@PathVariable String id) {
		roleService.updateRole(id,role);
	}
	@DeleteMapping(value ="/roles/{id}")
	public void deleteRole(@PathVariable String id) {
		roleService.deleteRole(id);
	}
}
