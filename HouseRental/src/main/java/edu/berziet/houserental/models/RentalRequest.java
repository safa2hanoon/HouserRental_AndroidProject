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
@Table(name = "rentalrequest")
public class RentalRequest {
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

	@Column(nullable = false)
	private int resultId;

	public RentalRequest() {
		super();
	}

	public RentalRequest(Property property, Tenant tenant) {
		super();
		this.property = property;
		this.tenant = tenant;
		this.resultId = 0;
	}

	public int getRequestId() {
		return id;
	}

	public void setRequestId(int requestId) {
		this.id = requestId;
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

	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}

	public String getResult() {
		switch (resultId) {
		case 1:
			return "Request Accepted";
		case 2:
			return "Request Rejected";
		}
		return "Awaiting Agency Approve";
	}
	public int getPropertyId() {
		if(property!=null) {
			return property.getId();
		}
		return 0;
	}
	public String getTenantId() {
		if(tenant!=null) {
			return tenant.getEmailAddress();
		}
		return "";
	}
	public String getTenantName() {
		if(tenant!=null) {
			return tenant.getFirstName()+" " + tenant.getLastName();
		}
		return "";
	}
}
