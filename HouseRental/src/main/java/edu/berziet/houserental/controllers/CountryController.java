package edu.berziet.houserental.controllers;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.berziet.houserental.models.City;
import edu.berziet.houserental.models.Country;
import edu.berziet.houserental.services.CountryService;

@RestController
public class CountryController {

	@Autowired
	CountryService countryService;
	
	@RequestMapping("/countries")
	public List<Country> getAllCountries() {
		return countryService.getAll();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/countries/{countryId}")
	public List<City> getCitiesListOfCountry(@PathVariable int countryId) {
		List<City> citiesList = new ArrayList<>();
		Country country = countryService.getById(countryId);
		if(country!=null) {
			citiesList = country.getCities();
		}
		return citiesList;
	}
}
