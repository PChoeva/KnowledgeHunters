package knowledgehunters.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import knowledgehunters.model.Topic;
import knowledgehunters.repository.TopicRepository;

@Service
public class TopicService {
	@Autowired TopicRepository topicRepository;
	
	public List<Topic> getAllTopics(){
		List<Topic> topics = new ArrayList<>();
		topicRepository.findAll().forEach(topics ::add);
		return topics;
	}
	
	public Optional<Topic> getTopic(int id){
		
		return  topicRepository.findById(id);
	}

	public void addTopic(Topic topic) {
		
		topicRepository.save(topic);
	}
	
	public void updateTopic(int id, Topic topic) {
		
		topicRepository.save(topic);
	}
	
	public void deleteTopic(int id) {
		
		topicRepository.deleteById(id);
	}
}


