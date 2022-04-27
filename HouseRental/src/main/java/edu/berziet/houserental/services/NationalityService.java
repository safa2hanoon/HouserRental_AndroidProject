package edu.berziet.houserental.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.berziet.houserental.models.Nationality;
import edu.berziet.houserental.repositories.NationalityRepository;


@Service
public class NationalityService {
	
	@Autowired
	private NationalityRepository nationalityRepository;
	
	public List<Nationality> getAll(){
		return nationalityRepository.findAll();
	}
	
	public Nationality getById(int nationalityId) {
		return nationalityRepository.getById(nationalityId);
	}

	public boolean isExistByNationalityId(int nationalityId) {
		Optional<Nationality> member = nationalityRepository.findById(nationalityId);
        return member.isPresent();
	}

	public void addNewNationality(Nationality nationality) {
		nationalityRepository.save(nationality);
	}

}
