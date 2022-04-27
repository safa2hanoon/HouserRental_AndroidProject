package edu.berziet.houserental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.berziet.houserental.models.Country;

public interface CountryRepository  extends JpaRepository<Country, Integer> {

}

