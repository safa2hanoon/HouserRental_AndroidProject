package edu.berziet.houserental.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.berziet.houserental.models.City;
import edu.berziet.houserental.models.Country;
import edu.berziet.houserental.models.Nationality;
import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.services.CityService;
import edu.berziet.houserental.services.CountryService;
import edu.berziet.houserental.services.NationalityService;
import edu.berziet.houserental.services.PropertyService;

@RestController
public class AppConnectController {

	@Autowired
	PropertyService propertyService;
	@Autowired
	CountryService countryService;
	@Autowired
	CityService cityService;
	@Autowired
	NationalityService nationalityService;
	

	@RequestMapping("/connect")
	public ConnectResponse connect() {
		return new ConnectResponse();
	}
	
	private class ConnectResponse{
		List<Property> propertiesList ;
		List<Country> countriesList;
		List<City> citiesList;
		List<Nationality> nationalitiesList;
		public ConnectResponse() {
			super();
			propertiesList = propertyService.getAllAvailable();
			countriesList = countryService.getAll();
			citiesList = cityService.getAll();
			nationalitiesList = nationalityService.getAll();
		}
		public List<Property> getPropertiesList() {
			return propertiesList;
		}
		public void setPropertiesList(List<Property> propertiesList) {
			this.propertiesList = propertiesList;
		}
		public List<Country> getCountriesList() {
			return countriesList;
		}
		public void setCountriesList(List<Country> countriesList) {
			this.countriesList = countriesList;
		}
		public List<City> getCitiesList() {
			return citiesList;
		}
		public void setCitiesList(List<City> citiesList) {
			this.citiesList = citiesList;
		}
		public List<Nationality> getNationalitiesList() {
			return nationalitiesList;
		}
		public void setNationalitiesList(List<Nationality> nationalitiesList) {
			this.nationalitiesList = nationalitiesList;
		}
		
	}
}
