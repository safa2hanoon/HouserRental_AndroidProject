package edu.berziet.houserental.models;

public class RentingAgencyProfileResponse {
	private boolean exist;
	private RentingAgency agencyProfile;
	public RentingAgencyProfileResponse() {
		super();
	}
	public boolean isExist() {
		return exist;
	}
	public void setExist(boolean exist) {
		this.exist = exist;
	}
	public RentingAgency getAgencyProfile() {
		return agencyProfile;
	}
	public void setAgencyProfile(RentingAgency agencyProfile) {
		this.agencyProfile = agencyProfile;
	}
}
