package edu.berziet.houserental.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.berziet.houserental.models.RentingAgency;
import edu.berziet.houserental.repositories.RentingAgencyRepository;

@Service
public class RentingAgencyService {
	@Autowired
	private RentingAgencyRepository repository;
	
	public RentingAgency getByEmail(String email) {
		return repository.getById(email);
	}
	
	public boolean isEmailExist(String email) {
		Optional<RentingAgency> agency = repository.findById(email);
		return agency.isPresent();
	}

	public RentingAgency addNewAgency(RentingAgency newAgency) {
		return repository.save(newAgency);
	}
	public RentingAgency updateAgency(RentingAgency newAgency) {
		return repository.save(newAgency);
	}
}
