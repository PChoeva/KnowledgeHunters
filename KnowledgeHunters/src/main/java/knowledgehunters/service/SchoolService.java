package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.School;
import knowledgehunters.repository.SchoolRepository;

@Service
public class SchoolService {
	@Autowired SchoolRepository schoolRepository;
	
	public List<School> getAllSchools(){
		List<School> schools = new ArrayList<>();
		schoolRepository.findAll().forEach(schools ::add);
		return schools;
	}
	
	public Optional<School> getSchool(String id){
		
		return  schoolRepository.findById(id);
	}

	public void addSchool(School school) {
		
		schoolRepository.save(school);
	}
	
	public void updateSchool(String id, School school) {
		schoolRepository.save(school);
		
	}
	
	public void deleteSchool(String id) {
		
		schoolRepository.deleteById(id);
		
	}
	
	
}


