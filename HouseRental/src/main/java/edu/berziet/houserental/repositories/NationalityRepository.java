package edu.berziet.houserental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.berziet.houserental.models.Nationality;

public interface NationalityRepository extends JpaRepository<Nationality,Integer> {

}
