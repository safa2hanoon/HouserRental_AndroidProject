package edu.berziet.houserental.models;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import edu.berziet.houserental.PasswordAuthentication;

@Entity
@Table(name = "tenants")
@JsonIgnoreProperties({"hashedPassword","requests"})
public class Tenant {

	@Id
	private String emailAddress;
	
	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;	

	@Column(nullable = false)
	private String hashedPassword;
	
	@Column(nullable = false)
	private String gender;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="nationality_id")
	private Nationality nationality;
	
	@Column(nullable = false)
	private int monthlySalary;
	
	@Column(nullable = false)
	private String occupation;

	@Column(nullable = false)
	private int familySize;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="country_id")
	private Country residenceCountry;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="city_id")
	private City residenceCity;

	@Column(nullable = false)
	private String phoneNumber;
	

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy="tenant")
	private List<RentalRequest> requests = new ArrayList<>();

	public Tenant() {
		super();
	}

	public Tenant(String emailAddress, String firstName, String lastName, String password, String gender,
			int monthlySalary, String occupation, int familySize, Country residenceCountry,
			City residenceCity,Nationality nationality, String phoneNumber) {
		super();
		this.emailAddress = emailAddress;
		this.firstName = firstName;
		this.lastName = lastName;
		this.setPassword(password);
		this.gender = gender;
		this.nationality = nationality;
		this.monthlySalary = monthlySalary;
		this.occupation = occupation;
		this.familySize = familySize;
		this.residenceCountry = residenceCountry;
		this.residenceCity = residenceCity;
		this.phoneNumber = phoneNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
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

	public void setPassword(String password) {
		PasswordAuthentication passAuth = new PasswordAuthentication();
		this.hashedPassword = passAuth.hash(password.toCharArray());
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public int getNationalityId() {
		if(getNationality()==null) {
			return 0;
		}
		return getNationality().getId();
	}

	public Nationality getNationality() {
		return nationality;
	}

	public void setNationality(Nationality nationality) {
		this.nationality = nationality;
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

	public Country getResidenceCountry() {
		return residenceCountry;
	}

	public void setResidenceCountry(Country residenceCountry) {
		this.residenceCountry = residenceCountry;
	}

	public City getResidenceCity() {
		return residenceCity;
	}

	public void setResidenceCity(City residenceCity) {
		this.residenceCity = residenceCity;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public int getCountryId() {
		Country c = getResidenceCountry();
		if(c!=null) {
			return c.getId();
		}
		return 0;
	}
	
	public String getCountryName() {
		Country c = getResidenceCountry();
		if(c!=null) {
			return c.getName();
		}
		return "";
	}

	
	public int getCityId() {
		City c = getResidenceCity();
		if(c!=null) {
			return c.getId();
		}
		return 0;
	}
	
	public String getCityName() {
		City c = getResidenceCity();
		if(c!=null) {
			return c.getName();
		}
		return "";
	}
	
	public String getTenantNationality() {
		if(getNationality()!=null) {
			return getNationality().getNationality();
		}
		return "";
	}

	public String getHashedPassword() {
		return hashedPassword;
	}
	
	
}
