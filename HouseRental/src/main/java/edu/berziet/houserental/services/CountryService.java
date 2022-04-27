package edu.berziet.houserental.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.berziet.houserental.models.Country;
import edu.berziet.houserental.repositories.CountryRepository;

@Service
public class CountryService {
	@Autowired
	private CountryRepository countryRepository;

	public List<Country> getAll() {
		return countryRepository.findAll();
	}

	public Country getById(int countryId) {
		return countryRepository.getById(countryId);
	}

	public boolean isExistByCountryId(int countryId) {
		Optional<Country> country = countryRepository.findById(countryId) ;
		return country.isPresent();
	}

	public void addNewCountry(Country country) {
		countryRepository.save(country);
	}

}
