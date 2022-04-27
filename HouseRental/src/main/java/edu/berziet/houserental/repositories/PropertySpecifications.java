package edu.berziet.houserental.repositories;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import edu.berziet.houserental.models.City;
import edu.berziet.houserental.models.Country;
import edu.berziet.houserental.models.Property;

public class PropertySpecifications {

	public static Specification<Property> propertyIsRented(boolean isRented){		
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("rented"),isRented);				
			}			
		};
	}
	

	public static Specification<Property> propertyInCountry(Country country){
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Path<City> city = root.<City> get("city");
				return criteriaBuilder.equal(city.get("country"),country);							
			}			
		};
	}
	

	public static Specification<Property> propertyInCity(City city){		
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("city"),city);				
			}			
		};
	}

	public static Specification<Property> propertyMinimumSurfaceArea(int minArea){		
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {		
				return criteriaBuilder.greaterThanOrEqualTo(root.get("surfaceArea"),minArea);				
			}			
		};
	}
	public static Specification<Property> propertyMaximumSurfaceArea(int maxArea){		
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {		
				return criteriaBuilder.lessThanOrEqualTo(root.get("surfaceArea"),maxArea);				
			}			
		};
	}
	public static Specification<Property> propertyMinimumBedroomsCount(int min){		
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("bedroomsCount"),min);				
			}			
		};
	}
	public static Specification<Property> propertyMaximumBedroomsCount(int max){		
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("bedroomsCount"),max);				
			}			
		};
	}
	public static Specification<Property> propertyMinimumRentalPrice(int min){		
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("rentalPrice"),min);				
			}			
		};
	}
	public static Specification<Property> propertyMaximumRentalPrice(int max){		
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.lessThanOrEqualTo(root.get("rentalPrice"),max);				
			}			
		};
	}

	public static Specification<Property> propertyStatus(String status){		
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("status"),status);				
			}			
		};
	}

	public static Specification<Property> propertyHasBalcony(boolean hasBalcony){		
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("hasBalcony"),hasBalcony);				
			}			
		};
	}

	public static Specification<Property> propertyHasGarden(boolean hasGarden){		
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("hasGarden"),hasGarden);				
			}			
		};
	}
	
	public static Specification<Property> advertiseDateBeforeDays(int maxDaysForAd){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -maxDaysForAd);
		Date d = cal.getTime();
		return new Specification<Property>(){
			@Override
			public Predicate toPredicate(Root<Property> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.greaterThanOrEqualTo(root.get("advertiseDate"),d);				
			}			
		};
	}

}
