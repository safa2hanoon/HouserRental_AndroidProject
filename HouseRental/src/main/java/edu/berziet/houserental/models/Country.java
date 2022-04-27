package edu.berziet.houserental.models;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "countries")
@JsonIgnoreProperties({"cities","tenants"})
public class Country {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "country_id")
	private Integer id = null;
	
	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false, unique = true)
	private String zipCode;
	
	@JsonManagedReference
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy="country")
	private List<City> cities = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy="residenceCountry")
	private List<Tenant> tenants = new ArrayList<>();

	public Country() {
		super();
	}

	public Country(Integer id, String name, String zipCode) {
		super();
		this.id = id;
		this.name = name;
		this.zipCode = zipCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public List<City> getCities() {
		return cities;
	}
	
	
}
