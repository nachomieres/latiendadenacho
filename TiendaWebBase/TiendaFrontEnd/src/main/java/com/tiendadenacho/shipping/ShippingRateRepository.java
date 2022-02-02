package com.tiendadenacho.shipping;

import org.springframework.data.repository.CrudRepository;

import com.tiendadenacho.entidades.Country;
import com.tiendadenacho.entidades.ShippingRate;

public interface ShippingRateRepository extends CrudRepository<ShippingRate, Integer> {
	
	public ShippingRate findByCountryAndState(Country country, String state);

}
