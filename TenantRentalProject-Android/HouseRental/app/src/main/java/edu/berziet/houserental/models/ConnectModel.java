package edu.berziet.houserental.models;

import java.util.List;

public class ConnectModel {
    List<PropertyModel> propertiesList;
    List<CountryModel> countriesList;
    List<CityModel> citiesList;
    List<NationalityModel> nationalitiesList;

    public ConnectModel() {

    }

    public List<PropertyModel> getPropertiesList() {
        return propertiesList;
    }

    public void setPropertiesList(List<PropertyModel> propertiesList) {
        this.propertiesList = propertiesList;
    }

    public List<CountryModel> getCountriesList() {
        return countriesList;
    }

    public void setCountriesList(List<CountryModel> countriesList) {
        this.countriesList = countriesList;
    }

    public List<CityModel> getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(List<CityModel> citiesList) {
        this.citiesList = citiesList;
    }

    public List<NationalityModel> getNationalitiesList() {
        return nationalitiesList;
    }

    public void setNationalitiesList(List<NationalityModel> nationalitiesList) {
        this.nationalitiesList = nationalitiesList;
    }
}
