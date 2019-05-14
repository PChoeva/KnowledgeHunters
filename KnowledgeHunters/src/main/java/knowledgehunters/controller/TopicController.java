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

import knowledgehunters.model.Topic;
import knowledgehunters.service.TopicService;


@RestController
public class TopicController {
	@Autowired
	private TopicService topicService;
	
	@GetMapping ("/topics") 
	public List<Topic> getAllTopics() {
		return  topicService.getAllTopics();
	}
	
	@GetMapping ("/topics/{id}") 
	public Optional<Topic> getTopicId(@PathVariable int id) {	
		return  topicService.getTopic(id);
	}
	
	@PostMapping(value="/topics")
	public void addTopic(@RequestBody Topic topic){
		
		topicService.addTopic(topic);	
	}
	
	@PutMapping(value ="/topics/{id}")
	public void updateTopic(@RequestBody Topic topic,@PathVariable int id) {
		topicService.updateTopic(id, topic);
	}
	
	@DeleteMapping(value ="/topics/{id}")
	public void deleteTopic(@PathVariable int id) {
		topicService.deleteTopic(id);
	}
}
