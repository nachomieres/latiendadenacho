package com.tiendadenacho.address;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.tiendadenacho.entidades.Address;
import com.tiendadenacho.entidades.Country;
import com.tiendadenacho.entidades.Customer;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class AddressRepositoryTests {

	@Autowired private AddressRepository repo;
	
	@Test
	public void testAddNew() {
		Integer customerId = 9;
		Integer countryId = 69; // España
		
		Address newAddress = new Address();
		newAddress.setCustomer(new Customer(customerId));
		newAddress.setCountry(new Country(countryId));
		newAddress.setFirstName("Graciano");
		newAddress.setLastName("Diaz Megido");
		newAddress.setPhoneNumber("696784882");
		newAddress.setAddressLine1("Calle Pravia 2, 2 Derecha");
		newAddress.setAddressLine2("Escalera Izquierda");
		newAddress.setCity("Oviedo");
		newAddress.setState("Asturias");
		newAddress.setPostalCode("33012");
		
		Address savedAddress = repo.save(newAddress);
		
		assertThat(savedAddress).isNotNull();
		assertThat(savedAddress.getId()).isGreaterThan(0);
	}
}
