package edu.berziet.houserental;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import edu.berziet.houserental.models.City;
import edu.berziet.houserental.models.Country;
import edu.berziet.houserental.repositories.CityRepository;
import edu.berziet.houserental.repositories.CountryRepository;

@SpringBootApplication
public class HouseRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseRentalApplication.class, args);
	}

}
