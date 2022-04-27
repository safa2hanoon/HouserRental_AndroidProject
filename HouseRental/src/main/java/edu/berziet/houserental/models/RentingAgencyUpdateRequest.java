package edu.berziet.houserental.models;

public class RentingAgencyUpdateRequest {
	private String name;
	private int countryId;
	private int cityId;
	private String phoneNumber;
	
	public RentingAgencyUpdateRequest() {
		super();
	}

	public RentingAgencyUpdateRequest(String name, int countryId, int cityId, String phoneNumber) {
		super();
		this.name = name;
		this.countryId = countryId;
		this.cityId = cityId;
		this.phoneNumber = phoneNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
