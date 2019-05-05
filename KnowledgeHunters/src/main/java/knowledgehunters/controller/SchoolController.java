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

import knowledgehunters.model.School;
import knowledgehunters.service.SchoolService;


@RestController
public class SchoolController {
	@Autowired
	private SchoolService schoolService;
	
	@GetMapping ("/schools") 
	public List<School> getAllSchools() {
		return  schoolService.getAllSchools();
	}
	
	@GetMapping ("/schools/{id}") 
	public Optional<School> getSchoolId(@PathVariable String id) {	
		return  schoolService.getSchool(id);
	}
	
	@PostMapping(value="/schools")
	public void addSchool(@RequestBody School school){
		
		schoolService.addSchool(school);	
	}
	
	@PutMapping(value ="/schools/{id}")
	public void updateSchool(@RequestBody School school,@PathVariable String id) {
		schoolService.updateSchool(id, school);
	}
	
	@DeleteMapping(value ="/schools/{id}")
	public void deleteSchool(@PathVariable String id) {
		schoolService.deleteSchool(id);
	}
}
