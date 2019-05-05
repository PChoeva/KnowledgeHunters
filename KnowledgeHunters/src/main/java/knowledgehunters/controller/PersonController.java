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

import knowledgehunters.model.Person;
import knowledgehunters.service.PersonService;


@RestController
public class PersonController {
	@Autowired
	private PersonService personService;
	
	@GetMapping ("/people") 
	public List<Person> getAllPeople() {
		return  personService.getAllPeople();
	}
	
	@GetMapping ("/people/{id}") 
	public Optional<Person> getPersonId(@PathVariable String id) {	
		return  personService.getPerson(id);
	}
	
	@PostMapping(value="/people")
	public void addPerson(@RequestBody Person person){
		
		personService.addPerson(person);	
	}
	
	@PutMapping(value ="/people/{id}")
	public void updatePerson(@RequestBody Person person,@PathVariable String id) {
		personService.updatePerson(id, person);
	}
	
	@DeleteMapping(value ="/people/{id}")
	public void deletePerson(@PathVariable String id) {
		personService.deletePerson(id);
	}
}
