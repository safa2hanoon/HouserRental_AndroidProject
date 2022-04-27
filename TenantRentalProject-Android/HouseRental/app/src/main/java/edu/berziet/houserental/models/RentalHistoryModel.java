package edu.berziet.houserental.models;

public class RentalHistoryModel {
    private int id;
    private String tenantId;
    private String tenantName;
    private PropertyModel property;

    public RentalHistoryModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public PropertyModel getProperty() {
        return property;
    }

    public void setProperty(PropertyModel property) {
        this.property = property;
    }
}
