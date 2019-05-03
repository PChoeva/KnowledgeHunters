package knowledgehunters.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import knowledgehunters.model.Role;
import knowledgehunters.service.RoleService;

@RestController
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@RequestMapping ("/roles") 
	public List<Role> getAllTopics() {
		return  roleService.getAllRoles();
	}
	@RequestMapping ("/roles/{id}") 
	public Optional<Role> getRoleId(@PathVariable String id) {
		
		
		return  roleService.getRole(id);
	}
	@RequestMapping(method=RequestMethod.POST, value="/roles")
	public void addRole(@RequestBody Role role){
		
		roleService.addRole(role);	
	}
	@RequestMapping(method=RequestMethod.PUT, value ="/roles/{id}")
	public void updateRole(@RequestBody Role role,@PathVariable String id) {
		roleService.updateRole(id,role);
	}
	@RequestMapping(method=RequestMethod.DELETE, value ="/roles/{id}")
	public void deleteRole(@PathVariable String id) {
		roleService.deleteRole(id);
	}
}
