package com.github.lusoalex.talkapisecurity;

import com.github.lusoalex.talkapisecurity.model.User;
import com.github.lusoalex.talkapisecurity.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

import java.util.Arrays;

@EnableWebFlux
@SpringBootApplication
public class ApiSecurityApplication implements CommandLineRunner {

	private Logger LOG = LoggerFactory.getLogger("ApiSecurityApplication");

	private final UserService userService;

	@Autowired
	public ApiSecurityApplication(UserService userService) {
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiSecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//init some data
		User admin = new User("f6cea05f-83f8-4506-8ea0-5f83f85506e6","admin","admin","admin","Decathlon");
		User user1 = new User("f90df750-3069-48e6-8df7-50306988e691","Alexandre","Faria","lusoalex","lusoalex");
		User user2 = new User("dd4611d2-c689-4598-8611-d2c689a5988d","Ferdinand","Magellan","ferdinand","ferdinand");
		User user3 = new User("d62bb82f-69aa-446f-abb8-2f69aa546f74","Bartolomeu","Dias","bartolomeu","bartolomeu");
		User user4 = new User("0bca690b-c5bb-4ec2-8a69-0bc5bb3ec209","Amalia","Rodrigues","amalia","amalia");
		User user5 = new User("0a74c71b-fd98-4c99-b4c7-1bfd981c9912","Luis","Figo","luis","luis");
		LOG.info("Inserting default data in in-memory database.");
		userService.save(Arrays.asList(admin,user1,user2,user3,user4,user5)).subscribe();
	}
}
