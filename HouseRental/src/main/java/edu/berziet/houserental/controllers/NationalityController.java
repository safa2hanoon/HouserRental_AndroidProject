package edu.berziet.houserental.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.berziet.houserental.models.Nationality;
import edu.berziet.houserental.services.NationalityService;

@RestController
public class NationalityController {
	@Autowired
	NationalityService nationalityService;
	
	@RequestMapping("/nationalities")
	public List<Nationality> getAllCountries() {
		return nationalityService.getAll();
	}
}
