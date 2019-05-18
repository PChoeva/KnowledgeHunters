package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Person;
import knowledgehunters.model.User;
import knowledgehunters.repository.PersonRepository;

@Service
public class PersonService {
	@Autowired PersonRepository personRepository;
	private Person sessionPerson;
	
	public List<Person> getAllPeople(){
		List<Person> people = new ArrayList<>();
		personRepository.findAll().forEach(people ::add);
		return people;
	}
	
	public Optional<Person> getPerson(int id){
		
		return  personRepository.findById(id);
	}

	public void addPerson(Person person) {
		
		personRepository.save(person);
	}
	public void updatePerson(int id, Person person) {
		personRepository.save(person);
		
	}
	public void deletePerson(int id) {
		
		personRepository.deleteById(id);
		
	}
	
	public Person findPersonByUserID(int userID) {
		List<Person> people = new ArrayList<>();
		personRepository.findAll().forEach(people ::add);
		for(Person person : people) {
			if(person.getUser().getId() == userID) {
				return person;
			}
		}
		return null;
	}
	
	public void saveSessionPerson(Person sessionPerson) {
		this.sessionPerson = sessionPerson;
	}
	
	public Person getSessionPerson() {
		return this.sessionPerson;
	}
	
	
}


