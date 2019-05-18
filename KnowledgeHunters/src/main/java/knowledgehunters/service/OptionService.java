package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Option;
import knowledgehunters.repository.OptionRepository;

@Service
public class OptionService {
	@Autowired OptionRepository optionRepository;
	
	public List<Option> getAllOptions(){
		List<Option> options = new ArrayList<>();
		optionRepository.findAll().forEach(options ::add);
		return options;
	}
	
	public Optional<Option> getOption(int id){
		
		return  optionRepository.findById(id);
	}

	public void addOption(Option option) {
		
		optionRepository.save(option);
	}
	
	public void updateOption(int id, Option option) {
		
		optionRepository.save(option);
	}
	
	public void deleteOption(int id) {
		
		optionRepository.deleteById(id);
	}
}


