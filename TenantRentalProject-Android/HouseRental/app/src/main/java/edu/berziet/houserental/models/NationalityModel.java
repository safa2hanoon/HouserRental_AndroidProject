package edu.berziet.houserental.models;

public class NationalityModel {
    private int nationalityId;
    private String nationality;

    public NationalityModel(int nationalityId, String nationality) {
        this.nationalityId = nationalityId;
        this.nationality = nationality;
    }

    public NationalityModel() {

    }

    public int getNationalityId() {
        return nationalityId;
    }

    public void setNationalityId(int nationalityId) {
        this.nationalityId = nationalityId;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
