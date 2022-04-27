package edu.berziet.houserental.models;

public class TenantInfoResponse {
	private boolean exist;
	private Tenant tenant;
	public TenantInfoResponse(boolean exist, Tenant tenant) {
		super();
		this.exist = exist;
		this.tenant = tenant;
	}
	public boolean isExist() {
		return exist;
	}
	public void setExist(boolean exist) {
		this.exist = exist;
	}
	public Tenant getTenant() {
		return tenant;
	}
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
}
