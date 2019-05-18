package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Rank;
import knowledgehunters.repository.RankRepository;

@Service
public class RankService {
	@Autowired RankRepository rankRepository;
	
	public List<Rank> getAllRanks(){
		List<Rank> ranks = new ArrayList<>();
		rankRepository.findAll().forEach(ranks ::add);
		return ranks;
	}
	
	public Optional<Rank> getRank(int id){
		
		return  rankRepository.findById(id);
	}

	public void addRank(Rank rank) {
		
		rankRepository.save(rank);
	}
	
	public void updateRank(int id, Rank rank) {
		
		rankRepository.save(rank);
	}
	
	public void deleteRank(int id) {
		
		rankRepository.deleteById(id);
	}
}


