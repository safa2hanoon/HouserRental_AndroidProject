package edu.berziet.houserental.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.berziet.houserental.models.City;
import edu.berziet.houserental.services.CityService;

@RestController
public class CityController {

	@Autowired
	CityService cityService;
	
	@RequestMapping("/cities")
	public List<City> getAllCities() {
		return cityService.getAll();
	}
}
