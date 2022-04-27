package edu.berziet.houserental.models;

import java.io.Serializable;

public class RentalRequestModel implements Serializable {
    private int requestId;
    private int resultId;
    private String result;
    private String tenantId;
    private int propertyId;
    private String tenantName;
    private PropertyModel property;

    public String getTenantName() {
        return tenantName;
    }

    public PropertyModel getProperty() {
        return property;
    }

    public void setProperty(PropertyModel property) {
        this.property = property;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(int propertyId) {
        this.propertyId = propertyId;
    }
}
