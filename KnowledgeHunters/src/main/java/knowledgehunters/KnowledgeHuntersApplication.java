package knowledgehunters;

import org.hibernate.annotations.SQLUpdate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootConfiguration
@SpringBootApplication
public class KnowledgeHuntersApplication {

	public static void main(String[] args) {
		SpringApplication.run(KnowledgeHuntersApplication.class, args);
	}
}
