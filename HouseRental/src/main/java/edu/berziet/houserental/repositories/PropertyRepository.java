package edu.berziet.houserental.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.RentingAgency;

public interface PropertyRepository extends JpaRepository<Property,Integer>,JpaSpecificationExecutor{

	List<Property> findTopByRentedOrderByAdvertiseDateDesc(boolean rented);
	
	List<Property> findTop5ByRentedOrderByAdvertiseDateDesc(boolean rented);
	
	List<Property> findAllByRented(boolean rented);

	Page<Property> findAllByRented(boolean b, PageRequest of);

	ArrayList<Property> findByRentingAgency(RentingAgency agency);
	
	
}
