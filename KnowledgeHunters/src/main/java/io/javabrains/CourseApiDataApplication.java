package io.javabrains;

import org.hibernate.annotations.SQLUpdate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootConfiguration
@SpringBootApplication
public class CourseApiDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(CourseApiDataApplication.class, args);
	}
}
