package edu.berziet.houserental.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.RentalHistory;
import edu.berziet.houserental.models.RentingAgency;
import edu.berziet.houserental.models.Tenant;
import edu.berziet.houserental.repositories.RentalHistoryRepository;
import edu.berziet.houserental.repositories.RentalHistorySpecifications;

@Service
public class RentalHistoryService {
	@Autowired
	private RentalHistoryRepository rentalHistoryRepository;
	
	public RentalHistory addNewRentalHistory(RentalHistory rentalHistory) {
		return rentalHistoryRepository.save(rentalHistory);
	}
	
	public ArrayList<RentalHistory> getTenantRentalHistory(Tenant t){
		return rentalHistoryRepository.findAllByTenant(t);
	}
	public List<RentalHistory> getAgencyRentalHistory(RentingAgency agency){
		return rentalHistoryRepository.findAll(
				RentalHistorySpecifications.historyForAgency(agency)
				);
	}
	
	public List<RentalHistory> getRentalHistoryForProperty(Property p){
		return rentalHistoryRepository.findAllByProperty(p);
	}

	public void delete(RentalHistory rentalHistory) {
		rentalHistoryRepository.delete(rentalHistory);
	}
	
}
