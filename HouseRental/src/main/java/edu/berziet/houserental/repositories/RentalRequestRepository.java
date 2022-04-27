package edu.berziet.houserental.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.RentalRequest;
import edu.berziet.houserental.models.Tenant;

public interface RentalRequestRepository extends JpaRepository<RentalRequest, Integer>, JpaSpecificationExecutor<RentalRequest>{
	
	public RentalRequest findByTenantAndProperty(Tenant t, Property p);
	
	public ArrayList<RentalRequest> findAllByTenantAndResultId(Tenant t, int resultId); 
	
	public ArrayList<RentalRequest> findAllByProperty(Property p);
}
