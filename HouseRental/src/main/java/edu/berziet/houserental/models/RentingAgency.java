package edu.berziet.houserental.models;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import edu.berziet.houserental.PasswordAuthentication;

@Entity
@Table(name = "rentingagencies")
@JsonIgnoreProperties({"properties","hashedPassword"})
public class RentingAgency {
	 	
	@Id
	private String emailAddress;
	
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String hashedPassword;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="country_id")
	private Country country;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="city_id")
	private City city;

	@Column(nullable = false)
	private String phoneNumber;
	

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy="rentingAgency")
	public List<Property> properties = new ArrayList<>();
	

	public RentingAgency() {
		super();
	}

	public RentingAgency(String emailAddress, String name, String password, Country country, City city,
			String phoneNumber) {
		super();
		this.emailAddress = emailAddress;
		this.name = name;
		this.setPassword(password);
		this.country = country;
		this.city = city;
		this.phoneNumber = phoneNumber;
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

	public void setName(String agencyName) {
		this.name = agencyName;
	}


	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setPassword(String password) {
		PasswordAuthentication passAuth = new PasswordAuthentication();
		this.hashedPassword = passAuth.hash(password.toCharArray());
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public int getCountryId() {
		Country c = getCountry();
		if(c!=null) {
			return c.getId();
		}
		return 0;
	}
	
	public String getCountryName() {
		Country c = getCountry();
		if(c!=null) {
			return c.getName();
		}
		return "";
	}

	
	public int getCityId() {
		City c = getCity();
		if(c!=null) {
			return c.getId();
		}
		return 0;
	}
	
	public String getCityName() {
		City c = getCity();
		if(c!=null) {
			return c.getName();
		}
		return "";
	}
	
}
