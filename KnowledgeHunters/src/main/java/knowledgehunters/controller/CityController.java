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

import knowledgehunters.model.City;
import knowledgehunters.service.CityService;;


@RestController
public class CityController {
	@Autowired
	private CityService cityService;
	
	@GetMapping ("/cities") 
	public List<City> getAllCities() {
		return  cityService.getAllCities();
	}
	
	@GetMapping ("/cities/{id}") 
	public Optional<City> getCityId(@PathVariable String id) {	
		return  cityService.getCity(id);
	}
	
	@PostMapping(value="/cities")
	public void addCity(@RequestBody City city){
		
		cityService.addCity(city);	
	}
	
	@PutMapping(value ="/cities/{id}")
	public void updateCity(@RequestBody City city,@PathVariable String id) {
		cityService.updateCity(id, city);
	}
	
	@DeleteMapping(value ="/cities/{id}")
	public void deleteCity(@PathVariable String id) {
		cityService.deleteCity(id);
	}
}
