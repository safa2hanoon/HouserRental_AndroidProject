package edu.berziet.houserental.repositories;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.RentalHistory;
import edu.berziet.houserental.models.Tenant;

public interface RentalHistoryRepository extends JpaRepository<RentalHistory,Integer>, JpaSpecificationExecutor {

	ArrayList<RentalHistory> findAllByTenant(Tenant t);

	ArrayList<RentalHistory> findAllByProperty(Property p);
}
