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

import knowledgehunters.model.Ranking;
import knowledgehunters.service.RankingService;


@RestController
public class RankingController {
	@Autowired
	private RankingService rankingService;
	
	@GetMapping ("/rankings") 
	public List<Ranking> getAllRankings() {
		return  rankingService.getAllRankings();
	}
	
	@GetMapping ("/rankings/{id}") 
	public Optional<Ranking> getRankingId(@PathVariable int id) {	
		return  rankingService.getRanking(id);
	}
	
	@PostMapping(value="/rankings")
	public void addRanking(@RequestBody Ranking ranking){
		
		rankingService.addRanking(ranking);	
	}
	
	@PutMapping(value ="/rankings/{id}")
	public void updateRanking(@RequestBody Ranking ranking,@PathVariable int id) {
		rankingService.updateRanking(id, ranking);
	}
	
	@DeleteMapping(value ="/rankings/{id}")
	public void deleteRanking(@PathVariable int id) {
		rankingService.deleteRanking(id);
	}
}
