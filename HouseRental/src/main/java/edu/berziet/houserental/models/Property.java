package edu.berziet.houserental.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "properties")
@JsonIgnoreProperties({"requests","rentalHistory"})
public class Property {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "property_id")
	private Integer id = null;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="city_id")
	private City city;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="agency_id")
	private RentingAgency rentingAgency;
	
	@Column(name = "surface_area")
	private Integer surfaceArea;

	@Column(name = "bedrooms_count")
	private Integer bedroomsCount;
	
	@Column(name = "rental_price")
	private Integer rentalPrice;
	
	@Column
	private String status;	

	@Column(name = "has_balcony")
	private boolean hasBalcony;
	
	@Column(name = "has_garden")
	private boolean hasGarden;

	@Column(name = "construction_year")
	private Integer constructionYear;

	@Column(name = "availability_date")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date availabilityDate;
	
	@Column
	private String description;
	
	@Column
	private boolean rented;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private Date advertiseDate;
	

	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy="property")
	private List<PropertyImage> imagesList = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy="property")
	private List<RentalRequest> requests = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy="property")
	private List<RentalHistory> rentalHistory = new ArrayList<>();

	public Property() {
		super();
	}

	public Property(Integer surfaceArea, Integer bedroomsCount, Integer rentalPrice,
			String status, boolean hasBalcony, boolean hasGarden, Integer constructionYear, Date availabilityDate,
			String description, City city,RentingAgency rentingAgency) {
		super();
		this.surfaceArea = surfaceArea;
		this.bedroomsCount = bedroomsCount;
		this.rentalPrice = rentalPrice;
		this.status = status;
		this.hasBalcony = hasBalcony;
		this.hasGarden = hasGarden;
		this.constructionYear = constructionYear;
		this.availabilityDate = availabilityDate;
		this.description = description;
		this.city = city;
		this.rentingAgency = rentingAgency;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Integer getSurfaceArea() {
		return surfaceArea;
	}

	public void setSurfaceArea(Integer surfaceArea) {
		this.surfaceArea = surfaceArea;
	}

	public Integer getBedroomsCount() {
		return bedroomsCount;
	}

	public void setBedroomsCount(Integer bedroomsCount) {
		this.bedroomsCount = bedroomsCount;
	}

	public Integer getRentalPrice() {
		return rentalPrice;
	}

	public void setRentalPrice(Integer rentalPrice) {
		this.rentalPrice = rentalPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isHasBalcony() {
		return hasBalcony;
	}

	public void setHasBalcony(boolean hasBalcony) {
		this.hasBalcony = hasBalcony;
	}

	public boolean isHasGarden() {
		return hasGarden;
	}

	public void setHasGarden(boolean hasGarden) {
		this.hasGarden = hasGarden;
	}

	public Integer getConstructionYear() {
		return constructionYear;
	}

	public void setConstructionYear(Integer constructionYear) {
		this.constructionYear = constructionYear;
	}

	public Date getAvailabilityDate() {
		return availabilityDate;
	}

	public void setAvailabilityDate(Date availabilityDate) {
		this.availabilityDate = availabilityDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public RentingAgency getRentingAgency() {
		return rentingAgency;
	}

	public void setRentingAgency(RentingAgency rentingAgency) {
		this.rentingAgency = rentingAgency;
	}

	public Date getAdvertiseDate() {
		return advertiseDate;
	}

	public void setAdvertiseDate(Date advertiseDate) {
		this.advertiseDate = advertiseDate;
	}

	public boolean isRented() {
		return rented;
	}

	public void setRented(boolean rented) {
		this.rented = rented;
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
	
	public String getAgencyId() {
		RentingAgency agency = getRentingAgency();
		if(agency!=null) {
			return agency.getEmailAddress();
		}
		return "";
	}

	public List<PropertyImage> getImagesList() {
		return imagesList;
	}

	public void setImagesList(List<PropertyImage> imagesList) {
		this.imagesList = imagesList;
	}
	
	
	
}
