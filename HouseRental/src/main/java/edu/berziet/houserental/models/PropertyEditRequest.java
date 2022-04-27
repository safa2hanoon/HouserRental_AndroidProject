package edu.berziet.houserental.models;

import java.util.Date;

public class PropertyEditRequest{
	private Integer cityId;
	private String agencyId;
	private Integer surfaceArea;
	private Integer bedroomsCount;
	private Integer rentalPrice;
	private String status;	
	private String hasBalcony;
	private String hasGarden;
	private Integer constructionYear;
	private Date availabilityDate;
	private String description;
	public PropertyEditRequest() {
		super();
	}
	public PropertyEditRequest(int cityId, String agencyId, Integer surfaceArea, Integer bedroomsCount,
			Integer rentalPrice, String status, String hasBalcony, String hasGarden, Integer constructionYear,
			Date availabilityDate, String description) {
		super();
		this.cityId = cityId;
		this.agencyId = agencyId;
		this.surfaceArea = surfaceArea;
		this.bedroomsCount = bedroomsCount;
		this.rentalPrice = rentalPrice;
		this.status = status;
		this.hasBalcony = hasBalcony;
		this.hasGarden = hasGarden;
		this.constructionYear = constructionYear;
		this.availabilityDate = availabilityDate;
		this.description = description;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
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
	public String getHasBalcony() {
		return hasBalcony;
	}
	public void setHasBalcony(String hasBalcony) {
		this.hasBalcony = hasBalcony;
	}
	public String getHasGarden() {
		return hasGarden;
	}
	public void setHasGarden(String hasGarden) {
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
	
}
