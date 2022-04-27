package edu.berziet.houserental;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import edu.berziet.houserental.models.City;
import edu.berziet.houserental.models.Country;
import edu.berziet.houserental.models.Nationality;
import edu.berziet.houserental.models.Property;
import edu.berziet.houserental.models.PropertyImage;
import edu.berziet.houserental.models.RentingAgency;
import edu.berziet.houserental.models.Tenant;
import edu.berziet.houserental.services.CityService;
import edu.berziet.houserental.services.CountryService;
import edu.berziet.houserental.services.NationalityService;
import edu.berziet.houserental.services.PropertyImageService;
import edu.berziet.houserental.services.PropertyService;
import edu.berziet.houserental.services.RentingAgencyService;
import edu.berziet.houserental.services.TenantService;

@Component
public class DataLoader implements ApplicationRunner{
	private CountryService countryService;
	private CityService cityService;
	private NationalityService nationalityService;
	private TenantService tenantService;	
	private RentingAgencyService rentingAgencyService;
	private PropertyService propertyService;
	private PropertyImageService propertyImageServce;
    @Autowired
    public DataLoader(CountryService countryService,
    		CityService cityService,
    		NationalityService nationalityService,
    		TenantService tenantService,
    		RentingAgencyService rentingAgencyService,
    		PropertyService propertyService,
    		PropertyImageService propertyImageServce) {
        this.countryService = countryService;
        this.cityService = cityService;
        this.nationalityService = nationalityService;
        this.tenantService = tenantService;
        this.rentingAgencyService = rentingAgencyService;
        this.propertyService = propertyService;
        this.propertyImageServce = propertyImageServce;
    }
    
	@Override
	public void run(ApplicationArguments args) throws Exception {
		Nationality palestinian = new Nationality(1,"Palestinian");
		nationalityService.addNewNationality(palestinian);
		nationalityService.addNewNationality(new Nationality(2,"Jordanian"));
		nationalityService.addNewNationality(new Nationality(3,"Saudi"));
		nationalityService.addNewNationality(new Nationality(4,"lebaneese"));
		nationalityService.addNewNationality(new Nationality(5,"Egyptian"));
		nationalityService.addNewNationality(new Nationality(6,"Syrian"));
		
		
		Country palestine = new Country(1,"Palestine","00970");
		countryService.addNewCountry(palestine);		
		Country jordan = new Country(2,"Jordan","00962");
		countryService.addNewCountry(jordan);
		Country saudiArabia = new Country(3,"Saudi Arabia","00966");
		countryService.addNewCountry(saudiArabia);
		Country lebanon = new Country(4,"Lebanon ","00961");
		countryService.addNewCountry(lebanon);
		Country egypt = new Country(5,"Egypt","0020");
		countryService.addNewCountry(egypt);
		Country syria = new Country(6,"Syria","00963");
		countryService.addNewCountry(syria);
		
		City ramallah = new City(1,"Ramallah",palestine);
		cityService.addNewCity(ramallah);
		City jerusalim = new City(2,"Jerusalim",palestine);
		cityService.addNewCity(jerusalim);

		City amman = new City(3,"Amman",jordan);
		cityService.addNewCity(amman);
		City aqaba = new City(4,"Aqaba",jordan);
		cityService.addNewCity(aqaba);
		
		City mekkah = new City(5,"Mekkah",saudiArabia);
		cityService.addNewCity(mekkah);
		City jeddah = new City(6,"Jeddah",saudiArabia);
		cityService.addNewCity(jeddah);

		City beirut = new City(7,"Beirut",lebanon);
		cityService.addNewCity(beirut);
		City sayda = new City(8,"Sayda",lebanon);
		cityService.addNewCity(sayda);

		City cairo = new City(9,"Cairo",egypt);
		cityService.addNewCity(cairo);
		City jeezah = new City(10,"Jeezah",egypt);
		cityService.addNewCity(jeezah);

		City damascus = new City(11,"Damascus",syria);
		cityService.addNewCity(damascus);
		City halab = new City(12,"Halab",syria);
		cityService.addNewCity(halab);

	}

}
