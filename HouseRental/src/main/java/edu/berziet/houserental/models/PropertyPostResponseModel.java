package edu.berziet.houserental.models;

public class PropertyPostResponseModel {
	private boolean success;
	private String errorMessage;
	private int propertyId;

	public PropertyPostResponseModel(boolean success, String errorMessage) {
		super();
		this.success = success;
		this.errorMessage = errorMessage;
	}
	

	public PropertyPostResponseModel(boolean success, String errorMessage, int propertyId) {
		super();
		this.success = success;
		this.errorMessage = errorMessage;
		this.propertyId = propertyId;
	}


	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public int getPropertyId() {
		return propertyId;
	}


	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}
	
}
