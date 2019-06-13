package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Ranking;
import knowledgehunters.model.Student;
import knowledgehunters.repository.RankingRepository;

@Service
public class RankingService {
	@Autowired RankingRepository rankingRepository;
	
	public List<Ranking> getAllRankings(){
		List<Ranking> rankings = new ArrayList<>();
		rankingRepository.findAll().forEach(rankings ::add);
		return rankings;
	}
	
	public Optional<Ranking> getRanking(int id){
		
		return  rankingRepository.findById(id);
	}

	public void addRanking(Ranking ranking) {
		
		rankingRepository.save(ranking);
	}
	
	public void updateRanking(int id, Ranking ranking) {
		rankingRepository.save(ranking);
		
	}
	
	public void deleteRanking(int id) {
		
		rankingRepository.deleteById(id);
		
	}
	
	public Ranking findRankingBySubjectIdAndStudent(int subjectId, Student student) {
		for (Ranking ranking: rankingRepository.findAll()) {
			if (ranking.getSubject().getId() == subjectId && ranking.getStudent().getId() == student.getId()) {
				return ranking;
			}
		}
		return null;
	}
	
	public List<Ranking> findAllRankingsBySubjectId(int subjectId) {
		List<Ranking> rankings = new ArrayList<>();
		for (Ranking ranking: rankingRepository.findAll()) {
			if (ranking.getSubject().getId() == subjectId) {
				rankings.add(ranking);
			}
		}
		return rankings;
	}
	
}


