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

import knowledgehunters.model.Option;
import knowledgehunters.service.OptionService;


@RestController
public class OptionController {
	@Autowired
	private OptionService optionService;
	
	@GetMapping ("/options") 
	public List<Option> getAllOptions() {
		return  optionService.getAllOptions();
	}
	
	@GetMapping ("/options/{id}") 
	public Optional<Option> getOptionId(@PathVariable int id) {	
		return  optionService.getOption(id);
	}
	
	@PostMapping(value="/options")
	public void addOption(@RequestBody Option option){
		
		optionService.addOption(option);	
	}
	
	@PutMapping(value ="/options/{id}")
	public void updateOption(@RequestBody Option option,@PathVariable int id) {
		optionService.updateOption(id, option);
	}
	
	@DeleteMapping(value ="/options/{id}")
	public void deleteOption(@PathVariable int id) {
		optionService.deleteOption(id);
	}
}
