package edu.berziet.houserental.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "rentalhistory")
public class RentalHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	private Integer id = null;
	
	@ManyToOne
	@JoinColumn(name="property_id")
	private Property property;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="emailAddress")
	private Tenant tenant;

	public RentalHistory() {
		super();
	}

	public RentalHistory(Property property, Tenant tenant) {
		super();
		this.property = property;
		this.tenant = tenant;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getTenantName() {
		if(tenant!=null) {
			return tenant.getFirstName()+" " +tenant.getLastName();
		}
		return "";
	}
	public String getTenantId() {
		if(tenant!=null) {
			return tenant.getEmailAddress();
		}
		return "";
	}
	
}
