package edu.berziet.houserental.models;

public class PostRequestModel {
	private String tenantId;
	private int propertyId;
	public PostRequestModel() {
		super();
	}
	public PostRequestModel(String tenantId, int propertyId) {
		super();
		this.tenantId = tenantId;
		this.propertyId = propertyId;
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
