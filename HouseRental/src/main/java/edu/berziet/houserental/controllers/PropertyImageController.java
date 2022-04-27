package edu.berziet.houserental.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.models.SimpleSuccessResponse;
import edu.berziet.houserental.services.FIleStoreService;
import edu.berziet.houserental.services.PropertyImageService;
import edu.berziet.houserental.services.PropertyService;

@RestController
public class PropertyImageController {
	@Autowired
	private PropertyImageService propertyImageService;
	@Autowired
	private PropertyService propertyService;
	@Autowired
	private FIleStoreService fileStoreService;
	
	@RequestMapping("/properties/{propertyId}/images")
	public ArrayList<PropertyImage> getPropertyImages(@PathVariable int propertyId){
		ArrayList<PropertyImage> imagesList = propertyImageService.getPropertyImagesList(propertyId);
		return imagesList;
	}
	
	@PostMapping("/properties/{propertyId}/images")
	public SimpleSuccessResponse getPropertyImages(@PathVariable int propertyId,@RequestParam("bitmap") MultipartFile file){
		boolean propertyExist = propertyService.isPropertyExist(propertyId);
		if(propertyExist) {
			String response = fileStoreService.store(file);
			if(response.isEmpty()) {
				return new SimpleSuccessResponse(false,"Could not upload image!");
			}else {
				String imageFileName = response;
				Property property = propertyService.getById(propertyId);
				PropertyImage propertyImage = new PropertyImage();
				propertyImage.setFileName(imageFileName);
				propertyImage.setProperty(property);
				propertyImageService.addPropertyImage(propertyImage);				
				return new SimpleSuccessResponse(true,"Image Uploaded Successfully");
			}
		}else {
			return new SimpleSuccessResponse(false,"No Property with such id exists!");
		}
		
	}
	

	@RequestMapping("/images/{fileName}")
	public ResponseEntity<Resource> getImageFile(@PathVariable String fileName) {
		Resource file =  fileStoreService.load(fileName);
		return ResponseEntity.ok()
		        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	
	@DeleteMapping("/images/{imageId}")
	public SimpleSuccessResponse deleteImage(@PathVariable int imageId) {
		boolean exist = propertyImageService.checkImageExist(imageId);		
		if(exist) {
			propertyImageService.deleteImageById(imageId);
			return new SimpleSuccessResponse(true,"Image Deleted Successfully");
		}
		return new SimpleSuccessResponse(false,"No image with such id exists!");
	}

}
