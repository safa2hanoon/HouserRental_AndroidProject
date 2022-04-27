package edu.berziet.houserental.repositories;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.RentalHistory;
import edu.berziet.houserental.models.RentingAgency;

public class RentalHistorySpecifications {

	public static Specification<RentalHistory> historyForAgency(RentingAgency agency){
		return new Specification<RentalHistory>(){
			@Override
			public Predicate toPredicate(Root<RentalHistory> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				final Path<Property> property = root.<Property> get("property");
				return criteriaBuilder.equal(property.get("rentingAgency"),agency);			
			}			
		};
	}
}
