package edu.berziet.houserental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.berziet.houserental.models.City;

public interface CityRepository  extends JpaRepository<City, Integer> {

}
