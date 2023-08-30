package CardsRestApi;

import CardsRestApi.Auth.AuthenticationService;
import CardsRestApi.Dtos.*;
import CardsRestApi.Repositories.CardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;

import static CardsRestApi.Models.Role.ADMIN;
import static CardsRestApi.Models.Role.MEMBER;

@SpringBootApplication
public class CardsRestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsRestApiApplication.class, args);
	}



    @Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service,
			CardRepository cardrepository
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstName("Admin")
					.cards(new ArrayList<>())
					.lastname("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getToken());

			var manager = RegisterRequest.builder()
					.firstName("user")
					.cards(new ArrayList<>())
					.lastname("user")
					.email("user@mail.com")
					.password("password")
					.role(MEMBER)
					.build();
			System.out.println("user token: " + service.register(manager).getToken());



		};


	}

}
