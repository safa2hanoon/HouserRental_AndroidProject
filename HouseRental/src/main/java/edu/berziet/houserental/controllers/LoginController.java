package edu.berziet.houserental.controllers;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.berziet.houserental.PasswordAuthentication;
import edu.berziet.houserental.models.City;
import edu.berziet.houserental.models.RentingAgency;
import edu.berziet.houserental.models.Tenant;
import edu.berziet.houserental.services.CityService;
import edu.berziet.houserental.services.RentingAgencyService;
import edu.berziet.houserental.services.TenantService;

@RestController
public class LoginController {
	@Autowired
	private RentingAgencyService rentingAgencyService;
	@Autowired
	private TenantService tenantService;
	@Autowired
	private CityService cityService;

	@RequestMapping(method = RequestMethod.POST, value = "/login")
	public LoginResponseModel login(@RequestBody LoginRequestModel loginRequestModel) {
		
			LoginResponseModel responseModel = new LoginResponseModel();

			PasswordAuthentication passAuth = new PasswordAuthentication();
			boolean agencyExist = rentingAgencyService.isEmailExist(loginRequestModel.getEmailAddress());
			if(agencyExist) {
				RentingAgency rentingAgency = rentingAgencyService.getByEmail(loginRequestModel.getEmailAddress());
				boolean passwordMatches = passAuth.authenticate(loginRequestModel.getPassword().toCharArray(), rentingAgency.getHashedPassword());
				if(passwordMatches) {
					responseModel.setRole("RentingAgency");
					responseModel.setSuccess(true);
					responseModel.setCity(rentingAgency.getCity());
					responseModel.setName(rentingAgency.getName());
				}else {
					responseModel.setErrorMessage("Invalid Login Credentials");
				}
			}else {
				boolean tenantExist = tenantService.isEmailExist(loginRequestModel.getEmailAddress());
				if(tenantExist) {
					Tenant tenant = tenantService.getByEmail(loginRequestModel.getEmailAddress());
					boolean passwordMatches = passAuth.authenticate(loginRequestModel.getPassword().toCharArray(), tenant.getHashedPassword());
					if(passwordMatches) {
						responseModel.setSuccess(true);
						responseModel.setRole("Tenant");
						City city = cityService.getById(tenant.getCityId());	
						String name = tenant.getFirstName()+" "+tenant.getLastName();
						responseModel.setName(name);
						responseModel.setCity(city);
					}else {
						responseModel.setErrorMessage("Invalid Login Credentials");
					}
				}else {
					responseModel.setErrorMessage("Invalid Login Credentials");
				}
			}
			return responseModel;
	}

	private static class LoginRequestModel {
		private String emailAddress;
		private String password;

		public LoginRequestModel() {
			super();
		}

		public LoginRequestModel(String emailAddress, String password) {
			this.emailAddress = emailAddress;
			this.password = password;
		}

		public String getEmailAddress() {
			return emailAddress;
		}

		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

	}

	private class LoginResponseModel {
		private boolean Success;
		private String errorMessage="";
		private String role = "";
		private String name="";
		private City city;

		public LoginResponseModel() {
			super();
		}
	

		public LoginResponseModel(boolean success, String errorMessage) {
			super();
			Success = success;
			this.errorMessage = errorMessage;
		}

		public void setCity(City city) {
			this.city=city;
		}

		public boolean isSuccess() {
			return Success;
		}

		public void setSuccess(boolean success) {
			Success = success;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public String getRole() {
			return role;
		}

		public void setRole(String role) {
			this.role = role;
		}
		
		

		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public int getCityId() {
			if(this.city!=null) {
				return this.city.getId();
			}
			return 0;
		}
		public String getCityName() {
			if(this.city!=null) {
				return this.city.getName();
			}
			return null;
		}
		public String getCountryName() {
			if(this.city!=null) {
				return this.city.getCountry().getName();
			}
			return null;
		}
		public int getCountryId() {
			if(this.city!=null) {
				return this.city.getCountryId();
			}
			return 0;
		}
	}
}
