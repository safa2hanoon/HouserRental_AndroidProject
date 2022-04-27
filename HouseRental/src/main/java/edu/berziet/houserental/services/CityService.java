package edu.berziet.houserental.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.berziet.houserental.models.City;
import edu.berziet.houserental.repositories.CityRepository;

@Service
public class CityService {
	@Autowired
	private CityRepository cityRepository;

	public List<City> getAll() {
		return cityRepository.findAll();
	}

	public boolean isExistByCityId(int cityId) {
		Optional<City> city = cityRepository.findById(cityId);
		return city.isPresent();
	}

	public City getById(int cityId) {
		return cityRepository.getById(cityId);
	}

	public void addNewCity(City city) {
		cityRepository.save(city);
	}
}
