package edu.berziet.houserental.models;

public class TenantUpdateRequestModel {
	
	private String firstName;

	private String lastName;	
	
	private String gender;
	
	private int nationalityId;
	
	private int monthlySalary;
	
	private String occupation;

	private int familySize;

	private int residenceCountryId;
	
	private int residenceCityId;

	private String phoneNumber;

	public TenantUpdateRequestModel() {
		super();
	}

	public TenantUpdateRequestModel(String firstName, String lastName, String gender,
			int nationalityId, int monthlySalary, String occupation, int familySize, int residenceCountryId,
			int residenceCityId, String phoneNumber) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.nationalityId = nationalityId;
		this.monthlySalary = monthlySalary;
		this.occupation = occupation;
		this.familySize = familySize;
		this.residenceCountryId = residenceCountryId;
		this.residenceCityId = residenceCityId;
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(int nationalityId) {
		this.nationalityId = nationalityId;
	}

	public int getMonthlySalary() {
		return monthlySalary;
	}

	public void setMonthlySalary(int monthlySalary) {
		this.monthlySalary = monthlySalary;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public int getFamilySize() {
		return familySize;
	}

	public void setFamilySize(int familySize) {
		this.familySize = familySize;
	}

	public int getResidenceCountryId() {
		return residenceCountryId;
	}

	public void setResidenceCountryId(int residenceCountryId) {
		this.residenceCountryId = residenceCountryId;
	}

	public int getResidenceCityId() {
		return residenceCityId;
	}

	public void setResidenceCityId(int residenceCityId) {
		this.residenceCityId = residenceCityId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
}
