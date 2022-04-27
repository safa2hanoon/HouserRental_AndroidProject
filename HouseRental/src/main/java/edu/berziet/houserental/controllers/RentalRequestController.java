package edu.berziet.houserental.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.berziet.houserental.services.PropertyService;
import edu.berziet.houserental.services.RentalHistoryService;
import edu.berziet.houserental.services.RentalRequestService;
import edu.berziet.houserental.services.RentingAgencyService;
import edu.berziet.houserental.services.TenantService;
import edu.berziet.houserental.models.PostRequestModel;
import edu.berziet.houserental.models.RentalRequest;
import edu.berziet.houserental.models.RentingAgency;
import edu.berziet.houserental.models.SimpleSuccessResponse;
import edu.berziet.houserental.models.Tenant;
import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.RentalHistory;

@RestController
public class RentalRequestController {

	@Autowired
	private RentalRequestService rentalRequestService;
	@Autowired
	private RentingAgencyService rentingAgencyService;
	@Autowired
	private TenantService tenantService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private RentalHistoryService rentalHistoryService;
	
	@RequestMapping("/requests")
	public List<RentalRequest> getAllRequests(){
		return rentalRequestService.findAll();
	}

	@RequestMapping("/requests/agency/{agencyId}")
	public List<RentalRequest> getAgencyRequests(@PathVariable String agencyId){
		RentingAgency agency = rentingAgencyService.getByEmail(agencyId);
		return rentalRequestService.getRentingAgencyRequests(agency);
	}

	@RequestMapping("/requests/tenant/{tenantId}")
	public List<RentalRequest> getTenantRequests(@PathVariable String tenantId){
		Tenant tenant = tenantService.getByEmail(tenantId);
		return rentalRequestService.getTenantRequests(tenant);
	}
	
	@PostMapping("/requests")
	public SimpleSuccessResponse postRequest(@RequestBody PostRequestModel request) {
		boolean tenantExist = tenantService.isEmailExist(request.getTenantId());
		if(!tenantExist) {
			return new SimpleSuccessResponse(false,"No tenant with such id exists!");
		}
		boolean propertyExist = propertyService.isPropertyExist(request.getPropertyId());
		if(!propertyExist) {
			return new SimpleSuccessResponse(false,"No property with such id exists!");
		}
		Tenant tenant = tenantService.getByEmail(request.getTenantId());
		Property p = propertyService.getById(request.getPropertyId());
		RentalRequest rentalRequest = new RentalRequest(p,tenant);
		rentalRequestService.addNewRentalRequest(rentalRequest);
		return new SimpleSuccessResponse(true,"Request applied successfully");
	}
	
	@RequestMapping("/requests/tenant/{tenantId}/{propertyId}")
	public RentalRequest checkTenantPropertyRequest(@PathVariable String tenantId,@PathVariable int propertyId) {
		Tenant t = tenantService.getByEmail(tenantId);
		Property p = propertyService.getById(propertyId);
		return rentalRequestService.getRequestForTenantAndProperty(t, p);
	}
	
	@RequestMapping(value="/requests/{requestId}")
	public RentalRequest getRequestDetails(@PathVariable int requestId) {
		return rentalRequestService.getById(requestId);
	}
	@PostMapping(value="/requests/{requestId}/approve")
	public SimpleSuccessResponse approveRequest(@PathVariable int requestId) {
		if(rentalRequestService.isRequestExist(requestId)) {
			RentalRequest request = rentalRequestService.getById(requestId);
			request.setResultId(1);
			rentalRequestService.updateRentalRequest(request);
			Property p = request.getProperty();
			p.setRented(true);
			propertyService.updateProperty(p);
			RentalHistory rentalHistory = new RentalHistory();
			rentalHistory.setProperty(p);
			rentalHistory.setTenant(request.getTenant());
			rentalHistoryService.addNewRentalHistory(rentalHistory);
			return new SimpleSuccessResponse(true,"Request Approved Successfully");
		}else {
			return new SimpleSuccessResponse(false,"No request with such id exists!");
		}
	}
	@PostMapping(value="/requests/{requestId}/reject")
	public SimpleSuccessResponse rejectRequest(@PathVariable int requestId) {
		if(rentalRequestService.isRequestExist(requestId)) {
			RentalRequest request = rentalRequestService.getById(requestId);
			request.setResultId(2);
			rentalRequestService.updateRentalRequest(request);
			return new SimpleSuccessResponse(true,"Request Rejected Successfully");
		}else {
			return new SimpleSuccessResponse(false,"No request with such id exists!");
		}
	}
	
	
}
