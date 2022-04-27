package edu.berziet.houserental.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.berziet.houserental.models.Tenant;
import edu.berziet.houserental.repositories.TenantRepository;

@Service
public class TenantService {
	@Autowired
	private TenantRepository tenantRepository;

	public Tenant addNewTenant(Tenant tenant) {
		return tenantRepository.save(tenant);
	}
	
	public boolean isEmailExist(String emailAddress) {
		Optional<Tenant> tenant = tenantRepository.findById(emailAddress);
		return tenant.isPresent();
	}

	public Tenant getByEmail(String emailAddress) {
		return tenantRepository.findById(emailAddress).get();
	}

	public void updateTenant(Tenant tenantToUpdate) {
		tenantRepository.save(tenantToUpdate);		
	}
}
