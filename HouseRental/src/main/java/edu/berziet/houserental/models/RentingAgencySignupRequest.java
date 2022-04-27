package edu.berziet.houserental.models;

public class RentingAgencySignupRequest {
	private String emailAddress;
	private String name;
	private String password;
	private int countryId;
	private int cityId;
	private String phoneNumber;
	public RentingAgencySignupRequest(String emailAddress, String name, String password, int countryId, int cityId,
			String phoneNumber) {
		super();
		this.emailAddress = emailAddress;
		this.name = name;
		this.password = password;
		this.countryId = countryId;
		this.cityId = cityId;
		this.phoneNumber = phoneNumber;
	}
	public RentingAgencySignupRequest() {
		super();
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
