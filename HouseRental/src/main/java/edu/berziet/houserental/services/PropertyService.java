package edu.berziet.houserental.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import edu.berziet.houserental.models.City;
import edu.berziet.houserental.models.Country;
import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.RentingAgency;
import edu.berziet.houserental.repositories.CityRepository;
import edu.berziet.houserental.repositories.PropertyRepository;
import edu.berziet.houserental.repositories.PropertySpecifications;

@Service
public class PropertyService {
	@Autowired
	private PropertyRepository propertyRepository;
	@Autowired
	private CityService cityService;
	@Autowired
	private CountryService countryService;
	
	public List<Property> getAllAvailable(){
		return propertyRepository.findAll(
				PropertySpecifications.propertyIsRented(false)
				.and(PropertySpecifications.advertiseDateBeforeDays(90))
				);
	}
	
	public List<Property> getAll(){
		return propertyRepository.findAll();
	}
	
	public Optional<Property> findById(int pId) {
		return propertyRepository.findById(pId);
	}
	
	public Property addNewProprty(Property newProperty) {
		return propertyRepository.save(newProperty);
	}

	public List<Property> getTopFive() {
		return propertyRepository.findTop5ByRentedOrderByAdvertiseDateDesc(false);
	}

	public Property getById(int propertyId) {		
		return propertyRepository.findById(propertyId).get();
	}

	public boolean isPropertyExist(int propertyId) {
		Optional<Property> property = propertyRepository.findById(propertyId);
		return property.isPresent();
	}

	public void updateProperty(Property propertyToUpdate) {
		propertyRepository.save(propertyToUpdate);	
	}

	public ArrayList<Property> findByAgency(RentingAgency agency) {		
		return propertyRepository.findByRentingAgency(agency);
	}
	
	public void deleteProperty(Property property) {		
		propertyRepository.delete(property);
	}
	public List<Property> searchProperties(int countryId,int cityId,int minSurfaceArea,int maxSurfaceArea,int minBedroomsCount,
			int maxBedroomsCount,int minRentalPrice,int maxRentalPrice,String status,
			boolean hasBalcony, boolean hasGarden){
		Specification<Property> ps = PropertySpecifications.propertyIsRented(false);	
		if(countryId!=0) {
			Country country = countryService.getById(countryId);
			ps = ps.and(PropertySpecifications.propertyInCountry(country));
		}
		if(cityId!=0) {
			City city = cityService.getById(cityId);
			ps = ps.and(PropertySpecifications.propertyInCity(city));
		}
		if(minSurfaceArea>0) {
			ps = ps.and(PropertySpecifications.propertyMinimumSurfaceArea(minSurfaceArea));
		}
		if(maxSurfaceArea>0) {
			ps = ps.and(PropertySpecifications.propertyMaximumSurfaceArea(maxSurfaceArea));
		}
		if(minBedroomsCount>0) {
		ps = ps.and(PropertySpecifications.propertyMinimumBedroomsCount(minBedroomsCount));	
		}
		if(maxBedroomsCount>0) {
		ps = ps.and(PropertySpecifications.propertyMaximumBedroomsCount(maxBedroomsCount));
		}
		if(minRentalPrice>0) {
		ps = ps.and(PropertySpecifications.propertyMinimumRentalPrice(minRentalPrice));
		}
		if(maxRentalPrice>0) {
		ps = ps.and(PropertySpecifications.propertyMaximumRentalPrice(maxRentalPrice));
		}
		if(status!=null && (status.equalsIgnoreCase("furnished") || status.equalsIgnoreCase("unfurnished"))) {
			ps = ps.and(PropertySpecifications.propertyStatus(status));	
		}
			ps = ps.and(PropertySpecifications.propertyHasBalcony(hasBalcony));
			ps = ps.and(PropertySpecifications.propertyHasGarden(hasGarden));
		
		return propertyRepository.findAll(ps);
	}
}
