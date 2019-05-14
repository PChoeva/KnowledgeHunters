package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.City;
import knowledgehunters.repository.CityRepository;

@Service
public class CityService {
	@Autowired CityRepository cityRepository;
	
	public List<City> getAllCities(){
		List<City> cities = new ArrayList<>();
		cityRepository.findAll().forEach(cities ::add);
		return cities;
	}
	
	public Optional<City> getCity(int id){
		
		return  cityRepository.findById(id);
	}

	public void addCity(City city) {
		
		cityRepository.save(city);
	}
	
	public void updateCity(int id, City city) {
		
		cityRepository.save(city);
	}
	
	public void deleteCity(int id) {
		
		cityRepository.deleteById(id);
	}
}


