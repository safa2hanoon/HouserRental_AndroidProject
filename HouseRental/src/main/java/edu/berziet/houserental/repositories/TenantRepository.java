package edu.berziet.houserental.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.berziet.houserental.models.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, String> {

}
