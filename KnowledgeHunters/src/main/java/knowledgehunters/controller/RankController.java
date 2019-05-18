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

import knowledgehunters.model.Rank;
import knowledgehunters.service.RankService;


@RestController
public class RankController {
	@Autowired
	private RankService rankService;
	
	@GetMapping ("/ranks") 
	public List<Rank> getAllRanks() {
		return  rankService.getAllRanks();
	}
	
	@GetMapping ("/ranks/{id}") 
	public Optional<Rank> getRankId(@PathVariable int id) {	
		return  rankService.getRank(id);
	}
	
	@PostMapping(value="/ranks")
	public void addRank(@RequestBody Rank rank){
		
		rankService.addRank(rank);	
	}
	
	@PutMapping(value ="/ranks/{id}")
	public void updateRank(@RequestBody Rank rank,@PathVariable int id) {
		rankService.updateRank(id, rank);
	}
	
	@DeleteMapping(value ="/ranks/{id}")
	public void deleteRank(@PathVariable int id) {
		rankService.deleteRank(id);
	}
}
