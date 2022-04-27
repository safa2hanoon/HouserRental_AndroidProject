package edu.berziet.houserental.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.PropertyEditRequest;
import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.models.PropertyPostModel;
import edu.berziet.houserental.models.PropertyPostResponseModel;
import edu.berziet.houserental.models.PropertySearchRequestModel;
import edu.berziet.houserental.models.RentalHistory;
import edu.berziet.houserental.models.RentalRequest;
import edu.berziet.houserental.models.SimpleSuccessResponse;
import edu.berziet.houserental.services.CityService;
import edu.berziet.houserental.services.PropertyImageService;
import edu.berziet.houserental.services.PropertyService;
import edu.berziet.houserental.services.RentalHistoryService;
import edu.berziet.houserental.services.RentalRequestService;
import edu.berziet.houserental.services.RentingAgencyService;

@RestController
public class PropertyController {

	@Autowired
	PropertyService propertyService;
	@Autowired
	CityService cityService;
	@Autowired
	RentingAgencyService agencyService;
	@Autowired
	PropertyImageService propertyImageService;
	@Autowired
	RentalRequestService rentalRequestService;
	@Autowired
	RentalHistoryService rentalHistoryService;

	@RequestMapping("/properties")
	public List<Property> getProperties() {
		List<Property> propertiesList = propertyService.getAllAvailable();
		return propertiesList;
	}
	@RequestMapping("/properties/top")
	public List<Property> getRecentPostedProperties() {
		List<Property> propertiesList = propertyService.getTopFive();
		return propertiesList;
	}

	@RequestMapping("/properties/available")
	public List<Property> getAvailable() {
		List<Property> propertiesList = propertyService.getAllAvailable();
		return propertiesList;
	}

	@RequestMapping("/properties/all")
	public List<Property> getAll() {
		List<Property> propertiesList = propertyService.getAll();
		return propertiesList;
	}
	@RequestMapping("/properties/search")
	public List<Property> searchProperties(@RequestBody PropertySearchRequestModel searchRequest) {
		List<Property> propertiesList = propertyService.searchProperties(
				searchRequest.getCountryId(),
				searchRequest.getCityId(),
				searchRequest.getMinSurfaceArea(),
				searchRequest.getMaxSurfaceArea(),
				searchRequest.getMinBedroomsCount(),
				searchRequest.getMaxBedroomsCount(),
				searchRequest.getMinRentalPrice(),
				searchRequest.getMaxRentalPrice(),
				searchRequest.getStatus(),
				searchRequest.isHasBalcony(),
				searchRequest.isHasGarden()
				);
		return propertiesList;
	}

	@RequestMapping("/properties/{propertyId}")
	public Property getPropertyInfo(@PathVariable int propertyId) {
		Optional<Property> p = propertyService.findById(propertyId);
		if (p.isPresent()) {
			return p.get();
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/properties")
	public PropertyPostResponseModel postProperty(@RequestBody PropertyPostModel propertyPostModel) {
		Property newProperty = new Property();
		// validate city id
		if (propertyPostModel.getCityId() == null) {
			return new PropertyPostResponseModel(false, "cityId value is required");
		} else {
			int cityId = propertyPostModel.getCityId();
			if (cityId <= 0) {
				return new PropertyPostResponseModel(false, "cityId value is not valid");
			} else if (!cityService.isExistByCityId(cityId)) {
				return new PropertyPostResponseModel(false, "cityId value does not exist");
			} else {
				newProperty.setCity(cityService.getById(cityId));
			}
		}

		// validate agencyId
		String agencyId = propertyPostModel.getAgencyId();
		if (agencyId == null || agencyId.isEmpty()) {
			return new PropertyPostResponseModel(false, "agencyId is required");
		} else if (!agencyService.isEmailExist(agencyId)) {
			return new PropertyPostResponseModel(false, "agencyId is not valid");
		} else {
			newProperty.setRentingAgency(agencyService.getByEmail(agencyId));
		}

		// validate surfaceArea
		if (propertyPostModel.getSurfaceArea() == null) {
			return new PropertyPostResponseModel(false, "surface area is required");
		} else if (propertyPostModel.getSurfaceArea() <= 0) {
			return new PropertyPostResponseModel(false, "surface area value is not valid");
		} else {
			newProperty.setSurfaceArea(propertyPostModel.getSurfaceArea());
		}

		// check bedrooms count
		if (propertyPostModel.getBedroomsCount() == null) {
			return new PropertyPostResponseModel(false, "Bedrooms count value is required");
		} else if (propertyPostModel.getBedroomsCount() <= 0) {
			return new PropertyPostResponseModel(false, "Bedrooms count value is not valid");
		} else {
			newProperty.setBedroomsCount(propertyPostModel.getBedroomsCount());
		}

		// check rental price
		if (propertyPostModel.getRentalPrice() == null) {
			return new PropertyPostResponseModel(false, "Rental price value is required");
		} else if (propertyPostModel.getRentalPrice() <= 0) {
			return new PropertyPostResponseModel(false, "Rental price value is not valid");
		} else {
			newProperty.setRentalPrice(propertyPostModel.getRentalPrice());
		}
		// check status
		String status = propertyPostModel.getStatus();
		if (status == null || status.isEmpty()) {
			return new PropertyPostResponseModel(false, "status value is required");
		} else if (status.equalsIgnoreCase("furnished") || status.equalsIgnoreCase("unfurnished")) {
			newProperty.setStatus(status);
		} else {
			return new PropertyPostResponseModel(false,
					"status value is not valid, must be either furnished or unfurnished");
		}
		// check has balcony
		String hasBalcony = propertyPostModel.getHasBalcony();
		if (hasBalcony == null || hasBalcony.isEmpty()) {
			return new PropertyPostResponseModel(false, "hasBalcony value is required");
		} else if (hasBalcony.equalsIgnoreCase("yes")) {
			newProperty.setHasBalcony(true);
		} else if (hasBalcony.equalsIgnoreCase("no")) {
			newProperty.setHasBalcony(false);
		} else {
			return new PropertyPostResponseModel(false, "hasBalcony value is not valied, must be either yes or no");
		}

		// check has garden
		String hasGarden = propertyPostModel.getHasGarden();
		if (hasGarden == null || hasGarden.isEmpty()) {
			return new PropertyPostResponseModel(false, "hasGarden value is required");
		} else if (hasGarden.equalsIgnoreCase("yes")) {
			newProperty.setHasGarden(true);
		} else if (hasGarden.equalsIgnoreCase("no")) {
			newProperty.setHasGarden(false);
		} else {
			return new PropertyPostResponseModel(false, "hasGarden value is not valied, must be either yes or no");
		}
		// check constructionYear
		if (propertyPostModel.getConstructionYear() == null) {
			return new PropertyPostResponseModel(false, "Construction Year value is required");
		} else if (propertyPostModel.getConstructionYear() <= 1900) {
			return new PropertyPostResponseModel(false, "Construction Year value is not valid");
		} else {
			newProperty.setConstructionYear(propertyPostModel.getConstructionYear());
		}

		// check availablityDate
		Date availabilityDate = propertyPostModel.getAvailabilityDate();
		if (availabilityDate == null) {
			return new PropertyPostResponseModel(false, "AvailablityDate Date value is required");
		} else {
			newProperty.setAvailabilityDate(availabilityDate);
		}

		// check description
		String description = propertyPostModel.getDescription();
		if (description == null || description.isEmpty()) {
			return new PropertyPostResponseModel(false, "description value is required");

		} else {
			String[] words = description.split(" ");
			if (words.length < 20 || words.length > 200) {
				return new PropertyPostResponseModel(false, "description must be between 20 to 200 words");
			} else {
				newProperty.setDescription(description);

			}
		}

		Property addedProperty = propertyService.addNewProprty(newProperty);
		return new PropertyPostResponseModel(true, "", addedProperty.getId());

	}

	@PatchMapping(value = "/properties/{propertyId}")
	public SimpleSuccessResponse postProperty(@PathVariable int propertyId,
			@RequestBody PropertyEditRequest editRequest) {
		boolean exist = propertyService.isPropertyExist(propertyId);
		if (!exist) {
			return new SimpleSuccessResponse(false, "No Property with such id exist!");
		}

		Property propertyToUpdate = propertyService.getById(propertyId);
		// validate city id
		if (editRequest.getCityId() == null) {
			return new SimpleSuccessResponse(false, "cityId value is required");
		} else {
			int cityId = editRequest.getCityId();
			if (cityId <= 0) {
				return new SimpleSuccessResponse(false, "cityId value is not valid");
			} else if (!cityService.isExistByCityId(cityId)) {
				return new SimpleSuccessResponse(false, "cityId value does not exist");
			} else {
				propertyToUpdate.setCity(cityService.getById(cityId));
			}
		}

		// validate agencyId
		String agencyId = editRequest.getAgencyId();
		if (agencyId == null || agencyId.isEmpty()) {
			return new SimpleSuccessResponse(false, "agencyId is required");
		} else if (!agencyService.isEmailExist(agencyId)) {
			return new SimpleSuccessResponse(false, "agencyId is not valid");
		} else {
			propertyToUpdate.setRentingAgency(agencyService.getByEmail(agencyId));
		}

		// validate surfaceArea
		if (editRequest.getSurfaceArea() == null) {
			return new SimpleSuccessResponse(false, "surface area is required");
		} else if (editRequest.getSurfaceArea() <= 0) {
			return new SimpleSuccessResponse(false, "surface area value is not valid");
		} else {
			propertyToUpdate.setSurfaceArea(editRequest.getSurfaceArea());
		}

		// check bedrooms count
		if (editRequest.getBedroomsCount() == null) {
			return new SimpleSuccessResponse(false, "Bedrooms count value is required");
		} else if (editRequest.getBedroomsCount() <= 0) {
			return new SimpleSuccessResponse(false, "Bedrooms count value is not valid");
		} else {
			propertyToUpdate.setBedroomsCount(editRequest.getBedroomsCount());
		}

		// check rental price
		if (editRequest.getRentalPrice() == null) {
			return new SimpleSuccessResponse(false, "Rental price value is required");
		} else if (editRequest.getRentalPrice() <= 0) {
			return new SimpleSuccessResponse(false, "Rental price value is not valid");
		} else {
			propertyToUpdate.setRentalPrice(editRequest.getRentalPrice());
		}
		// check status
		String status = editRequest.getStatus();
		if (status == null || status.isEmpty()) {
			return new SimpleSuccessResponse(false, "status value is required");
		} else if (status.equalsIgnoreCase("furnished") || status.equalsIgnoreCase("unfurnished")) {
			propertyToUpdate.setStatus(status);
		} else {
			return new SimpleSuccessResponse(false,
					"status value is not valid, must be either furnished or unfurnished");
		}
		// check has balcony
		String hasBalcony = editRequest.getHasBalcony();
		if (hasBalcony == null || hasBalcony.isEmpty()) {
			return new SimpleSuccessResponse(false, "hasBalcony value is required");
		} else if (hasBalcony.equalsIgnoreCase("yes")) {
			propertyToUpdate.setHasBalcony(true);
		} else if (hasBalcony.equalsIgnoreCase("no")) {
			propertyToUpdate.setHasBalcony(false);
		} else {
			return new SimpleSuccessResponse(false, "hasBalcony value is not valied, must be either yes or no");
		}

		// check has garden
		String hasGarden = editRequest.getHasGarden();
		if (hasGarden == null || hasGarden.isEmpty()) {
			return new SimpleSuccessResponse(false, "hasGarden value is required");
		} else if (hasGarden.equalsIgnoreCase("yes")) {
			propertyToUpdate.setHasGarden(true);
		} else if (hasGarden.equalsIgnoreCase("no")) {
			propertyToUpdate.setHasGarden(false);
		} else {
			return new SimpleSuccessResponse(false, "hasGarden value is not valied, must be either yes or no");
		}
		// check constructionYear
		if (editRequest.getConstructionYear() == null) {
			return new SimpleSuccessResponse(false, "Construction Year value is required");
		} else if (editRequest.getConstructionYear() <= 1900) {
			return new SimpleSuccessResponse(false, "Construction Year value is not valid");
		} else {
			propertyToUpdate.setConstructionYear(editRequest.getConstructionYear());
		}

		// check availablityDate
		Date availabilityDate = editRequest.getAvailabilityDate();
		if (availabilityDate == null) {
			return new SimpleSuccessResponse(false, "AvailablityDate Date value is required");
		} else {
			propertyToUpdate.setAvailabilityDate(availabilityDate);
		}

		// check description
		String description = editRequest.getDescription();
		if (description == null || description.isEmpty()) {
			return new SimpleSuccessResponse(false, "description value is required");

		} else {
			String[] words = description.split(" ");
			if (words.length < 20 || words.length > 200) {
				return new SimpleSuccessResponse(false, "description must be between 20 to 200 words");
			} else {
				propertyToUpdate.setDescription(description);
			}
		}

		propertyToUpdate.setAdvertiseDate(Calendar.getInstance().getTime());
		propertyService.updateProperty(propertyToUpdate);
		return new SimpleSuccessResponse(true, "");

	}

	@DeleteMapping(value="/properties/{propertyId}")
	public SimpleSuccessResponse deleteProperty(@PathVariable int propertyId) {
		Optional<Property> propertyOptional = propertyService.findById(propertyId);
		if(propertyOptional.isPresent()) {
			List<PropertyImage> images = propertyImageService.getPropertyImagesList(propertyId);
			for (PropertyImage propertyImage : images) {
				propertyImageService.deleteImageById(propertyImage.getId());
			}
			Property p = propertyOptional.get();
			List<RentalRequest> requests = rentalRequestService.getRequestsForProperty(p);
			for (RentalRequest rentalRequest : requests) {
				rentalRequestService.delete(rentalRequest);
			}
			List<RentalHistory> historyList = rentalHistoryService.getRentalHistoryForProperty(p);
			for (RentalHistory rentalHistory : historyList) {
				rentalHistoryService.delete(rentalHistory);
			}			
			
			propertyService.deleteProperty(propertyOptional.get());
			return new SimpleSuccessResponse(true, "Property Deleted Successfully");
		}
		return new SimpleSuccessResponse(false, "No Property with such ID exists!");
	}
}
