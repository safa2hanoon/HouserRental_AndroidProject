package edu.berziet.houserental.models;

public class CountryModel {
    private int countryId;
    private String name;
    private String zipCode;

    public CountryModel(int countryId, String name, String zipCode) {
        this.countryId = countryId;
        this.name = name;
        this.zipCode = zipCode;
    }

    public CountryModel() {

    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
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

    @Override
    public String toString() {
        return "CountryModel{" +
                "countryId=" + countryId +
                ", name='" + name + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
