package edu.berziet.houserental.models;

import java.util.ArrayList;

public class PropertyModel {
    private int id ;
    private int cityId;
    private String agencyId;
    private Integer surfaceArea;
    private Integer bedroomsCount;
    private Integer rentalPrice;
    private String status;
    private boolean hasBalcony;
    private boolean hasGarden;
    private Integer constructionYear;
    private String availabilityDate;
    private String description;
    private boolean rented;
    private String advertiseDate;
    private ArrayList<PropertyImage> imagesList;

    public PropertyModel() {
        super();
    }

    public PropertyModel(int id,Integer surfaceArea, Integer bedroomsCount, Integer rentalPrice,
                    String status, boolean hasBalcony, boolean hasGarden, Integer constructionYear, String availabilityDate,
                    String description, int cityId,String agencyId,String advertiseDate,ArrayList<PropertyImage> imagesList) {
        this.id = id;
        this.surfaceArea = surfaceArea;
        this.bedroomsCount = bedroomsCount;
        this.rentalPrice = rentalPrice;
        this.status = status;
        this.hasBalcony = hasBalcony;
        this.hasGarden = hasGarden;
        this.constructionYear = constructionYear;
        this.availabilityDate = availabilityDate;
        this.description = description;
        this.cityId = cityId;
        this.agencyId = agencyId;
        this.advertiseDate = advertiseDate;
        this.imagesList = imagesList;
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

    public String getAvailabilityDate() {
        return availabilityDate;
    }

    public void setAvailabilityDate(String availabilityDate) {
        this.availabilityDate = availabilityDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdvertiseDate() {
        return advertiseDate;
    }

    public void setAdvertiseDate(String advertiseDate) {
        this.advertiseDate = advertiseDate;
    }

    public boolean isRented() {
        return rented;
    }

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
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

    public ArrayList<PropertyImage> getImagesList() {
        return imagesList;
    }

    public void setImagesList(ArrayList<PropertyImage> imagesList) {
        this.imagesList = imagesList;
    }
}
