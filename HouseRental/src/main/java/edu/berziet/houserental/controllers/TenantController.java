package edu.berziet.houserental.controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import edu.berziet.houserental.PasswordValidator;
import edu.berziet.houserental.models.SimpleSuccessResponse;
import edu.berziet.houserental.models.Tenant;
import edu.berziet.houserental.models.TenantInfoResponse;
import edu.berziet.houserental.models.TenantSignupRequest;
import edu.berziet.houserental.models.TenantUpdateRequestModel;
import edu.berziet.houserental.services.CityService;
import edu.berziet.houserental.services.CountryService;
import edu.berziet.houserental.services.NationalityService;
import edu.berziet.houserental.services.RentingAgencyService;
import edu.berziet.houserental.services.TenantService;

@RestController
public class TenantController {

	@Autowired
	private TenantService tenantService;
	@Autowired
	private NationalityService nationalityService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private CityService cityService;
	@Autowired
	private RentingAgencyService rentingAgencyServicc;

	@RequestMapping(method = RequestMethod.POST, value = "/tenants")
	public SimpleSuccessResponse signUp(@RequestBody TenantSignupRequest tenantRequest) {		
		Tenant newTenant = new Tenant();
		try {	
			// validate email is not null and not empty
			if(tenantRequest.getEmailAddress()==null || tenantRequest.getEmailAddress().isEmpty()) {
				return new SimpleSuccessResponse(false, "Email Address is required");
			}else	{
				Pattern pattern = Pattern.compile("^(.+)@([^@]+[^.])$");
		        Matcher matcher = pattern.matcher(tenantRequest.getEmailAddress());
				// validate email as valid email format		
		        if(!matcher.matches()) {
		        	return new SimpleSuccessResponse(false,  "Not valid email address");
		        }else // check if email is registered before
		        	if (tenantService.isEmailExist(tenantRequest.getEmailAddress()) ||
		        			rentingAgencyServicc.isEmailExist(tenantRequest.getEmailAddress())) {
		        		return new SimpleSuccessResponse(false,  "Email address is registered");
		        	
		        }else {
		        	newTenant.setEmailAddress(tenantRequest.getEmailAddress());
		        }	
			}
			// validate first name is not null or empty string and 3-20 chars
			if(tenantRequest.getFirstName()==null || tenantRequest.getFirstName().isEmpty()) {
				return new SimpleSuccessResponse(false,  "First name is required");
			}else if(tenantRequest.getFirstName().length()<3 || tenantRequest.getFirstName().length()>20) {
				return new SimpleSuccessResponse(false,  "First name must be between 3 and 20 characters long");
			}else {
				newTenant.setFirstName(tenantRequest.getFirstName());
			}
			// validate last name is not null or empty string, and 3-20 chars
			if(tenantRequest.getLastName()==null || tenantRequest.getLastName().isEmpty()) {
				return new SimpleSuccessResponse(false,  "Last name is required");
			}else if(tenantRequest.getLastName().length()<3 || tenantRequest.getLastName().length()>20) {
				return new SimpleSuccessResponse(false,  "Last name must be between 3 and 20 characters long");
			}else {
				newTenant.setLastName(tenantRequest.getLastName());
			}
			// validate gender
			if(tenantRequest.getGender()==null || tenantRequest.getGender().isEmpty()) {
				return new SimpleSuccessResponse(false,  "Gender is required");
			}else if(! (tenantRequest.getGender().equalsIgnoreCase("male") || tenantRequest.getGender().equalsIgnoreCase("female"))) {
				return new SimpleSuccessResponse(false,  "Gender must be either male or female");
			}else {
				newTenant.setGender(tenantRequest.getGender().toLowerCase());
			}
			
			// validate password
			if(tenantRequest.getPassword()==null || tenantRequest.getPassword().isEmpty()) {
				return new SimpleSuccessResponse(false,  "Password is required");
			}else if(!PasswordValidator.isValidPassword(tenantRequest.getPassword())) {
				return new SimpleSuccessResponse(false,  "Password does not meet the password policy");
			}else {
				newTenant.setPassword(tenantRequest.getPassword());
			}
			
			// validate nationality
			if(tenantRequest.getNationalityId()==0) {
				return new SimpleSuccessResponse(false, "Nationality value is required");
			}else {
				boolean nationalityExist = nationalityService.isExistByNationalityId(tenantRequest.getNationalityId());
				if(!nationalityExist) {
					return new SimpleSuccessResponse(false, "Nationality value is not valid");
				}else {
					newTenant.setNationality(nationalityService.getById(tenantRequest.getNationalityId()));
				}
			}
			// validate monthly salary
			if(tenantRequest.getMonthlySalary()<=0) {
				return new SimpleSuccessResponse(false,  "Monthly salary value is not valid");
			}else {
				newTenant.setMonthlySalary(tenantRequest.getMonthlySalary());
			}
			
			// validate occupation
			if(tenantRequest.getOccupation()==null || tenantRequest.getOccupation().isEmpty()) {
				return new SimpleSuccessResponse(false,  "Occupation is required");
			}else if(tenantRequest.getOccupation().length()>20) {
				return new SimpleSuccessResponse(false,  "Occupation lenght must not exceed 20 characters");
			}else {
				newTenant.setOccupation(tenantRequest.getOccupation());
			}
			
			// validate family Size
			if(tenantRequest.getFamilySize()<=0) {
				return new SimpleSuccessResponse(false,  "Family size value is not valid");
			}else {
				newTenant.setFamilySize(tenantRequest.getFamilySize());
			}
			
			// validate residence country
			if(tenantRequest.getResidenceCountryId()<=0) {
				return new SimpleSuccessResponse(false,  "Residence Country Id value is missing or not valid");
			}else {
				boolean countryExist = countryService.isExistByCountryId(tenantRequest.getResidenceCountryId());
				if(!countryExist) {
					return new SimpleSuccessResponse(false,  "Residence Country Id value is not valid");
				}else {
					newTenant.setResidenceCountry((countryService.getById(tenantRequest.getResidenceCountryId())));
				}
			}
			
			// validate residence city
			if(tenantRequest.getResidenceCityId()<=0) {
				return new SimpleSuccessResponse(false,  "Residence City Id value is missing or not valid");
			}else {
				boolean cityExist = cityService.isExistByCityId(tenantRequest.getResidenceCityId());
				if(!cityExist) {
					return new SimpleSuccessResponse(false,  "Residence City Id value is not valid");
				}else {
					newTenant.setResidenceCity((cityService.getById(tenantRequest.getResidenceCityId())));
				}
			}
			if(tenantRequest.getPhoneNumber()==null || tenantRequest.getPhoneNumber().isEmpty()) {
				return new SimpleSuccessResponse(false,  "Phone Number is required");
			}else {
				newTenant.setPhoneNumber(tenantRequest.getPhoneNumber());
			}
			
			tenantService.addNewTenant(newTenant);
		   } catch (Exception ex) {
		      ex.printStackTrace();
		      return new SimpleSuccessResponse(false, "Server Error, Exception!");
		   }finally {
			
		}
		
		
			return new SimpleSuccessResponse(true, "");
	}


	@RequestMapping(method = RequestMethod.GET, value = "/tenants/{tenantEmailAddress}")
	public TenantInfoResponse getTenantInfo(@PathVariable String tenantEmailAddress) {
		boolean exist = tenantService.isEmailExist(tenantEmailAddress);
		if(exist) {
			Tenant t =  tenantService.getByEmail(tenantEmailAddress);
			return new TenantInfoResponse(true, t);
		}
		return new TenantInfoResponse(false, null);
	}
	
	
	@PatchMapping(value="/tenants/{tenantId}")
	public SimpleSuccessResponse updateTenantInfo(@PathVariable String tenantId,@RequestBody TenantUpdateRequestModel tenantRequest) {
		boolean tenantExist = tenantService.isEmailExist(tenantId);
		if(tenantExist) {
			Tenant tenantToUpdate = tenantService.getByEmail(tenantId);
			try {	
				// validate first name is not null or empty string and 3-20 chars
				if(tenantRequest.getFirstName()==null || tenantRequest.getFirstName().isEmpty()) {
					return new SimpleSuccessResponse(false,  "First name is required");
				}else if(tenantRequest.getFirstName().length()<3 || tenantRequest.getFirstName().length()>20) {
					return new SimpleSuccessResponse(false,  "First name must be between 3 and 20 characters long");
				}else {
					tenantToUpdate.setFirstName(tenantRequest.getFirstName());
				}
				// validate last name is not null or empty string, and 3-20 chars
				if(tenantRequest.getLastName()==null || tenantRequest.getLastName().isEmpty()) {
					return new SimpleSuccessResponse(false,  "Last name is required");
				}else if(tenantRequest.getLastName().length()<3 || tenantRequest.getLastName().length()>20) {
					return new SimpleSuccessResponse(false,  "Last name must be between 3 and 20 characters long");
				}else {
					tenantToUpdate.setLastName(tenantRequest.getLastName());
				}
				// validate gender
				if(tenantRequest.getGender()==null || tenantRequest.getGender().isEmpty()) {
					return new SimpleSuccessResponse(false,  "Gender is required");
				}else if(! (tenantRequest.getGender().equalsIgnoreCase("male") || tenantRequest.getGender().equalsIgnoreCase("female"))) {
					return new SimpleSuccessResponse(false,  "Gender must be either male or female");
				}else {
					tenantToUpdate.setGender(tenantRequest.getGender().toLowerCase());
				}				
				
				// validate nationality
				if(tenantRequest.getNationalityId()==0) {
					return new SimpleSuccessResponse(false, "Nationality value is required");
				}else {
					boolean nationalityExist = nationalityService.isExistByNationalityId(tenantRequest.getNationalityId());
					if(!nationalityExist) {
						return new SimpleSuccessResponse(false, "Nationality value is not valid");
					}else {
						tenantToUpdate.setNationality(nationalityService.getById(tenantRequest.getNationalityId()));
					}
				}
				// validate monthly salary
				if(tenantRequest.getMonthlySalary()<=0) {
					return new SimpleSuccessResponse(false,  "Monthly salary value is not valid");
				}else {
					tenantToUpdate.setMonthlySalary(tenantRequest.getMonthlySalary());
				}
				
				// validate occupation
				if(tenantRequest.getOccupation()==null || tenantRequest.getOccupation().isEmpty()) {
					return new SimpleSuccessResponse(false,  "Occupation is required");
				}else if(tenantRequest.getOccupation().length()>20) {
					return new SimpleSuccessResponse(false,  "Occupation lenght must not exceed 20 characters");
				}else {
					tenantToUpdate.setOccupation(tenantRequest.getOccupation());
				}
				
				// validate family Size
				if(tenantRequest.getFamilySize()<=0) {
					return new SimpleSuccessResponse(false,  "Family size value is not valid");
				}else {
					tenantToUpdate.setFamilySize(tenantRequest.getFamilySize());
				}
				
				// validate residence country
				if(tenantRequest.getResidenceCountryId()<=0) {
					return new SimpleSuccessResponse(false,  "Residence Country Id value is missing or not valid");
				}else {
					boolean countryExist = countryService.isExistByCountryId(tenantRequest.getResidenceCountryId());
					if(!countryExist) {
						return new SimpleSuccessResponse(false,  "Residence Country Id value is not valid");
					}else {
						tenantToUpdate.setResidenceCountry((countryService.getById(tenantRequest.getResidenceCountryId())));
					}
				}
				
				// validate residence city
				if(tenantRequest.getResidenceCityId()<=0) {
					return new SimpleSuccessResponse(false,  "Residence City Id value is missing or not valid");
				}else {
					boolean cityExist = cityService.isExistByCityId(tenantRequest.getResidenceCityId());
					if(!cityExist) {
						return new SimpleSuccessResponse(false,  "Residence City Id value is not valid");
					}else {
						tenantToUpdate.setResidenceCity((cityService.getById(tenantRequest.getResidenceCityId())));
					}
				}
				if(tenantRequest.getPhoneNumber()==null || tenantRequest.getPhoneNumber().isEmpty()) {
					return new SimpleSuccessResponse(false,  "Phone Number is required");
				}else {
					tenantToUpdate.setPhoneNumber(tenantRequest.getPhoneNumber());
				}
				
				tenantService.updateTenant(tenantToUpdate);
			   } catch (Exception ex) {
			      ex.printStackTrace();
			   }
		}else {
			return new SimpleSuccessResponse(false,"No tenant with such id exists!");
		}
	      return new SimpleSuccessResponse(false, "Server Error, Exception!");
	}
}
