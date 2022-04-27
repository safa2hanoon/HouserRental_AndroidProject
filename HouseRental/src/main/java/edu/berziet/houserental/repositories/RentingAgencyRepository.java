package edu.berziet.houserental.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import edu.berziet.houserental.models.RentingAgency;

public interface RentingAgencyRepository extends JpaRepository<RentingAgency,String> {

}
