package edu.berziet.houserental.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.berziet.houserental.models.RentalHistory;
import edu.berziet.houserental.models.RentingAgency;
import edu.berziet.houserental.models.Tenant;
import edu.berziet.houserental.services.RentalHistoryService;
import edu.berziet.houserental.services.RentingAgencyService;
import edu.berziet.houserental.services.TenantService;

@RestController
public class RentalHistoryController {
	@Autowired
	private RentalHistoryService rentalHistoryService;
	@Autowired
	private TenantService tenantService;
	@Autowired
	private RentingAgencyService agencyService;
	
	@RequestMapping(value="/rentalhistory/tenant/{tenantId}")
	public List<RentalHistory> getTenantRentalHistory(@PathVariable String tenantId){
		Tenant t = tenantService.getByEmail(tenantId);
		return rentalHistoryService.getTenantRentalHistory(t);
	}

	@RequestMapping(value="/rentalhistory/agency/{agencyId}")
	public List<RentalHistory> getAgencyRentalHistory(@PathVariable String agencyId){
		RentingAgency ra = agencyService.getByEmail(agencyId);
		return rentalHistoryService.getAgencyRentalHistory(ra);
	}
}
