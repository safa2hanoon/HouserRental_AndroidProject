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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "nationalities")
@JsonIgnoreProperties({"tenants"})
public class Nationality {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "nationality_id")
	private Integer id = null;
	
	@Column(nullable = false, unique = true)
	private String nationality;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy="nationality")
	private List<Tenant> tenants = new ArrayList<>();

	public Nationality() {
		super();
	}

	
	
	public Nationality(Integer id, String nationality) {
		super();
		this.id = id;
		this.nationality = nationality;
	}



	public Nationality(Integer id, String nationality, List<Tenant> tenants) {
		super();
		this.id = id;
		this.nationality = nationality;
		this.tenants = tenants;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public List<Tenant> getTenants() {
		return tenants;
	}

}
