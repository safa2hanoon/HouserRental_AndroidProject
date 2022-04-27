package edu.berziet.houserental.controllers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.berziet.houserental.PasswordValidator;
import edu.berziet.houserental.models.City;
import edu.berziet.houserental.models.Country;
import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.RentingAgency;
import edu.berziet.houserental.models.RentingAgencyProfileResponse;
import edu.berziet.houserental.models.RentingAgencySignupRequest;
import edu.berziet.houserental.models.RentingAgencyUpdateRequest;
import edu.berziet.houserental.models.SimpleSuccessResponse;
import edu.berziet.houserental.services.CityService;
import edu.berziet.houserental.services.CountryService;
import edu.berziet.houserental.services.PropertyService;
import edu.berziet.houserental.services.RentingAgencyService;
import edu.berziet.houserental.services.TenantService;

@RestController
public class RentingAgencyController {
	@Autowired
	private RentingAgencyService rentingAgencyService;
	@Autowired
	private CountryService countryService;
	@Autowired
	private CityService cityService;
	@Autowired
	private TenantService tenantService;
	@Autowired
	private PropertyService propertyService;

	@RequestMapping(method = RequestMethod.POST, value = "/agencies")
	public SimpleSuccessResponse signUp(@RequestBody RentingAgencySignupRequest retAgencySignupRequest) {
		RentingAgency newAgency = new RentingAgency();
		// validate email address;
		try {
			// validate email is not null and not empty
			if (retAgencySignupRequest.getEmailAddress() == null || retAgencySignupRequest.getEmailAddress().isEmpty()) {
				return new SimpleSuccessResponse(false, "Email Address is required");
				
			} else {
				// validate email as valid email format
				Pattern pattern = Pattern.compile("^(.+)@([^@]+[^.])$");
				Matcher matcher = pattern.matcher(retAgencySignupRequest.getEmailAddress());
				if (!matcher.matches()) {
					return new SimpleSuccessResponse(false, "Not valid email address");
					
				} else // check if email is registered before
				if (rentingAgencyService.isEmailExist(retAgencySignupRequest.getEmailAddress()) ||
						tenantService.isEmailExist(retAgencySignupRequest.getEmailAddress())) {
					return new SimpleSuccessResponse(false,"Email address is registered");					
				} else {
					// email is valid email address
					newAgency.setEmailAddress(retAgencySignupRequest.getEmailAddress());
				}
			}
			// validate name is not null or empty string and 3-20 chars
			if (retAgencySignupRequest.getName() == null || retAgencySignupRequest.getName().isEmpty()) {
				return new SimpleSuccessResponse(false,"Name is required");
			} else if (retAgencySignupRequest.getName().length() < 3 || retAgencySignupRequest.getName().length() > 20) {
				return new SimpleSuccessResponse(false,"Name must be between 3 and 20 characters long");
			} else {
				newAgency.setName(retAgencySignupRequest.getName());
			}

			// validate password
			if (retAgencySignupRequest.getPassword() == null || retAgencySignupRequest.getPassword().isEmpty()) {
				return new SimpleSuccessResponse(false,"Password is required");
			} else if (!PasswordValidator.isValidPassword(retAgencySignupRequest.getPassword())) {
				return new SimpleSuccessResponse(false,"Password does not meet the password policy");
			} else {
				newAgency.setPassword(retAgencySignupRequest.getPassword());
			}

			// validate country
			if (retAgencySignupRequest.getCountryId() <= 0) {
				return new SimpleSuccessResponse(false,"Country Id value is missing or not valid");
			} else {
				boolean countryExist = countryService.isExistByCountryId(retAgencySignupRequest.getCountryId());
				if (!countryExist) {
					return new SimpleSuccessResponse(false,"Country Id value is not valid");
				} else {
					newAgency.setCountry((countryService.getById(retAgencySignupRequest.getCountryId())));
				}
			}

			// validate city
			if (retAgencySignupRequest.getCityId() <= 0) {
				return new SimpleSuccessResponse(false,"City Id value is missing or not valid");
			} else {
				boolean cityExist = cityService.isExistByCityId(retAgencySignupRequest.getCityId());
				if (!cityExist) {
					return new SimpleSuccessResponse(false,"Residence City Id value is not valid");
				} else {
					newAgency.setCity((cityService.getById(retAgencySignupRequest.getCityId())));
				}
			}
			
			//validate phone number
			if (retAgencySignupRequest.getPhoneNumber() == null || retAgencySignupRequest.getPhoneNumber().isEmpty()) {
				return new SimpleSuccessResponse(false,"Phone Number is required");
			} else {
				newAgency.setPhoneNumber(retAgencySignupRequest.getPhoneNumber());
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return new SimpleSuccessResponse(false,"Server Error, Exception occured");
		} finally {

		}
			rentingAgencyService.addNewAgency(newAgency);
			return new SimpleSuccessResponse(true,"");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/agencies/{agencyId}")
	public RentingAgencyProfileResponse getAgencyProfile(@PathVariable String agencyId) {
		RentingAgencyProfileResponse response = new RentingAgencyProfileResponse();
		boolean exist = rentingAgencyService.isEmailExist(agencyId);
		response.setExist(exist);
		if(exist) {
			RentingAgency agency = rentingAgencyService.getByEmail(agencyId);
			response.setAgencyProfile(agency);
		}
		return response;
	}
	
	@PatchMapping( value = "/agencies/{agencyId}")
	public SimpleSuccessResponse updateAgencyProfile(@PathVariable String agencyId,@RequestBody RentingAgencyUpdateRequest agencyUpdateRequest) {
		boolean exist = rentingAgencyService.isEmailExist(agencyId);		
		if(!exist) {
			return new SimpleSuccessResponse(false,"No Agency with such email exists!");
		}else {
			RentingAgency agency = rentingAgencyService.getByEmail(agencyId);
			if(agencyUpdateRequest.getName()==null || agencyUpdateRequest.getName().isEmpty()) {
				return new SimpleSuccessResponse(false,"Agency name is required");
			}
			agency.setName(agencyUpdateRequest.getName());
			Boolean countryExist = countryService.isExistByCountryId(agencyUpdateRequest.getCountryId());
			if(countryExist) {
				Country country = countryService.getById(agencyUpdateRequest.getCountryId());
				agency.setCountry(country);
			}else {
				return new SimpleSuccessResponse(false,"No country with such id exists!");
			}
			Boolean cityExist = cityService.isExistByCityId(agencyUpdateRequest.getCityId());
			if(cityExist) {
				City city = cityService.getById(agencyUpdateRequest.getCityId());
				agency.setCity(city);
			}else {
				return new SimpleSuccessResponse(false,"No city with such id exists!");
			}
			if(agencyUpdateRequest.getPhoneNumber()==null || agencyUpdateRequest.getPhoneNumber().isEmpty()) {
				return new SimpleSuccessResponse(false,"Agency phone number is required");
			}
			agency.setPhoneNumber(agencyUpdateRequest.getPhoneNumber());
			rentingAgencyService.updateAgency(agency);
			return new SimpleSuccessResponse(true,"Updated successfully");
		}
		
	}
	

	@RequestMapping( value = "/agencies/{agencyId}/properties")
	public ArrayList<Property> getAgencyProperties(@PathVariable String agencyId){		
		RentingAgency agency = rentingAgencyService.getByEmail(agencyId);
		ArrayList<Property> propertiesList = propertyService.findByAgency(agency);
		return propertiesList;
	}
}
