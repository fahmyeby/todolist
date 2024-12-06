package demo.ssf_practice_workshop;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import demo.ssf_practice_workshop.model.Todo;
import demo.ssf_practice_workshop.repo.RedisRepo;
import demo.ssf_practice_workshop.service.DatabaseService;

@SpringBootApplication
public class SsfPracticeWorkshopApplication implements CommandLineRunner {

	@Autowired DatabaseService databaseService;
	@Autowired RedisRepo repo;

	public static void main(String[] args) {
		SpringApplication.run(SsfPracticeWorkshopApplication.class, args);
	}
	@Override
	public void run (String... args) throws FileNotFoundException{
		String fileName = "todos.json";

		//read file
		List<Todo> todoList = databaseService.readFile(fileName);

		//display on console
		System.out.println("\nData received:\n");
		for (Todo todo : todoList){
			System.out.println("Saving data to Redis: " + todo);
			repo.save(todo);
			System.out.println("\nData saved to Redis: " + todo);
		}
	}
	

}
