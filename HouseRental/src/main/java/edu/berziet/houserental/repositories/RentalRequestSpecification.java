package edu.berziet.houserental.repositories;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.RentalRequest;
import edu.berziet.houserental.models.RentingAgency;
import edu.berziet.houserental.models.Tenant;

public class RentalRequestSpecification {

	public static Specification<RentalRequest> requestsForTenant(Tenant tenant){		
		return new Specification<RentalRequest>(){
			@Override
			public Predicate toPredicate(Root<RentalRequest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("tenant"),tenant);				
			}			
		};
	}

	public static Specification<RentalRequest> requestsForRentingAgency(RentingAgency agency){		
		return new Specification<RentalRequest>(){
			@Override
			public Predicate toPredicate(Root<RentalRequest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Path<Property> property = root.<Property> get("property");
				return criteriaBuilder.equal(property.get("rentingAgency"),agency);			
			}			
		};
	}
	
	public static Specification<RentalRequest> acceptedRequests(){
		return new Specification<RentalRequest>(){
			@Override
			public Predicate toPredicate(Root<RentalRequest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				return criteriaBuilder.equal(root.get("resultId"),1);				
			}			
		};
	}
}
