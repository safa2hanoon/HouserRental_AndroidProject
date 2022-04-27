package edu.berziet.houserental.models;

public class PropertySearchRequestModel {
	private int countryId;
	private int cityId;
	private int minSurfaceArea;
	private int maxSurfaceArea;
	private int minBedroomsCount;	
	private int maxBedroomsCount;
	private int minRentalPrice;
	private int maxRentalPrice;
	private String status;	
	private boolean hasBalcony;
	private boolean hasGarden;
	public PropertySearchRequestModel() {
		super();
	}
	public PropertySearchRequestModel(int countryId,int cityId, int minSurfaceArea, int maxSurfaceArea, int minBedroomsCount,
			int maxBedroomsCount, int minRentalPrice, int maxRentalPrice, String status, boolean hasBalcony,
			boolean hasGarden) {
		super();
		this.countryId = countryId;
		this.cityId = cityId;
		this.minSurfaceArea = minSurfaceArea;
		this.maxSurfaceArea = maxSurfaceArea;
		this.minBedroomsCount = minBedroomsCount;
		this.maxBedroomsCount = maxBedroomsCount;
		this.minRentalPrice = minRentalPrice;
		this.maxRentalPrice = maxRentalPrice;
		this.status = status;
		this.hasBalcony = hasBalcony;
		this.hasGarden = hasGarden;
	}
	
	public int getCountryId() {
		return countryId;
	}
	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public int getMinSurfaceArea() {
		return minSurfaceArea;
	}
	public void setMinSurfaceArea(int minSurfaceArea) {
		this.minSurfaceArea = minSurfaceArea;
	}
	public int getMaxSurfaceArea() {
		return maxSurfaceArea;
	}
	public void setMaxSurfaceArea(int maxSurfaceArea) {
		this.maxSurfaceArea = maxSurfaceArea;
	}
	public int getMinBedroomsCount() {
		return minBedroomsCount;
	}
	public void setMinBedroomsCount(int minBedroomsCount) {
		this.minBedroomsCount = minBedroomsCount;
	}
	public int getMaxBedroomsCount() {
		return maxBedroomsCount;
	}
	public void setMaxBedroomsCount(int maxBedroomsCount) {
		this.maxBedroomsCount = maxBedroomsCount;
	}
	public int getMinRentalPrice() {
		return minRentalPrice;
	}
	public void setMinRentalPrice(int minRentalPrice) {
		this.minRentalPrice = minRentalPrice;
	}
	public int getMaxRentalPrice() {
		return maxRentalPrice;
	}
	public void setMaxRentalPrice(int maxRentalPrice) {
		this.maxRentalPrice = maxRentalPrice;
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
	
}
