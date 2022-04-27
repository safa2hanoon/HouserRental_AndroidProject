package edu.berziet.houserental.services;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.repositories.PropertyImageRepository;

@Service
public class PropertyImageService {

	@Autowired
	private PropertyImageRepository propertyImageRepository;
	@Autowired
	private FIleStoreService fileStoreService;
	
	public ArrayList<PropertyImage> getPropertyImagesList(int propertyId){
		ArrayList<PropertyImage> imagesList = propertyImageRepository.findByPropertyId(propertyId);		
		return imagesList;
	}
	public PropertyImage addPropertyImage(PropertyImage image) {
		return propertyImageRepository.save(image);
	}
	public boolean checkImageExist(int imageId) {
		Optional<PropertyImage> imageOptional = propertyImageRepository.findById(imageId);
		return imageOptional.isPresent();
	}
	public PropertyImage getImageById(int imageId) {
		return propertyImageRepository.getById(imageId);
	}
	public void deleteImageById(int imageId) {
		PropertyImage pImage = getImageById(imageId);
		fileStoreService.deleteImage(pImage.getFileName());
		propertyImageRepository.deleteById(imageId);
	}
	
}
