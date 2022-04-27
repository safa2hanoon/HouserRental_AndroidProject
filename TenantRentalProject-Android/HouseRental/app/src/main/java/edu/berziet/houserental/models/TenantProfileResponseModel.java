package edu.berziet.houserental.models;

public class TenantProfileResponseModel {
    private Boolean exist;
    private TenantModel tenant;

    public TenantProfileResponseModel() {
    }

    public Boolean getExist() {
        return exist;
    }

    public void setExist(Boolean exist) {
        this.exist = exist;
    }

    public TenantModel getTenant() {
        return tenant;
    }

    public void setTenant(TenantModel tenant) {
        this.tenant = tenant;
    }
}
