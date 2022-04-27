package edu.berziet.houserental.models;

public class AgencyProfileResponseModel {
    private Boolean exist;
    private RentingAgencyModel agency;

    public AgencyProfileResponseModel() {
    }

    public Boolean getExist() {
        return exist;
    }

    public void setExist(Boolean exist) {
        this.exist = exist;
    }

    public RentingAgencyModel getAgency() {
        return agency;
    }

    public void setAgency(RentingAgencyModel agency) {
        this.agency = agency;
    }
}
