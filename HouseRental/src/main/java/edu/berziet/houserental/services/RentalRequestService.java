package edu.berziet.houserental.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.RentalRequest;
import edu.berziet.houserental.models.RentingAgency;
import edu.berziet.houserental.models.Tenant;
import edu.berziet.houserental.repositories.RentalRequestRepository;
import edu.berziet.houserental.repositories.RentalRequestSpecification;

@Service
public class RentalRequestService {

	@Autowired
	private RentalRequestRepository rentalRequestRepository;
	
	public RentalRequest addNewRentalRequest(RentalRequest request) {
		return rentalRequestRepository.save(request);
	}
	public RentalRequest updateRentalRequest(RentalRequest request) {
		return rentalRequestRepository.save(request);
	}
	public List<RentalRequest> getTenantRequests(Tenant tenant){		
		return rentalRequestRepository.findAll(
				RentalRequestSpecification.requestsForTenant(tenant)
				);
				
	}

	public List<RentalRequest> getRentingAgencyRequests(RentingAgency agency){		
		return rentalRequestRepository.findAll(
				RentalRequestSpecification.requestsForRentingAgency(agency)
				);
				
	}
	public List<RentalRequest> findAll() {
		return rentalRequestRepository.findAll();
	}
	
	public boolean isRequestExist(int requestId) {
		Optional<RentalRequest> req = rentalRequestRepository.findById(requestId);
		return req.isPresent();
	}
	
	public RentalRequest getRequestForTenantAndProperty(Tenant t, Property p) {
		return rentalRequestRepository.findByTenantAndProperty(t, p);
	}
	public RentalRequest getById(int requestId) {
		return rentalRequestRepository.findById(requestId).get();
	}
	
	public ArrayList<RentalRequest> getRequestsForProperty(Property p){
		return rentalRequestRepository.findAllByProperty(p);
	}
	public void delete(RentalRequest rentalRequest) {
		rentalRequestRepository.delete(rentalRequest);
	}
	
}
