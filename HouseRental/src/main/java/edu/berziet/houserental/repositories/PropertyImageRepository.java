package edu.berziet.houserental.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import antlr.collections.List;
import edu.berziet.houserental.models.PropertyImage;

public interface PropertyImageRepository extends JpaRepository<PropertyImage,Integer> {

	public ArrayList<PropertyImage> findByPropertyId(int propertyId);
}
