package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Person;
import knowledgehunters.repository.PersonRepository;

@Service
public class PersonService {
	@Autowired PersonRepository personRepository;
	
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
	
	
}


