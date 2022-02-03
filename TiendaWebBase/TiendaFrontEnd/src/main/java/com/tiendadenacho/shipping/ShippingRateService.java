package com.tiendadenacho.shipping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tiendadenacho.entidades.Address;
import com.tiendadenacho.entidades.Customer;
import com.tiendadenacho.entidades.ShippingRate;

@Service
public class ShippingRateService {

	@Autowired private ShippingRateRepository repo;
	
	public ShippingRate getShippingRateForCustomer(Customer customer) {
		String state = customer.getState();
		if (state == null || state.isEmpty()) {
			state = customer.getCity();
		}
		// Si la ubicacion es españa permite el envio con una tasa de 2€ por kilo y 2 dias de plazo de envio
		if (customer.getCountry().getCode().equals("ES")) {
			ShippingRate shippingRate =  new ShippingRate();
			shippingRate.setCodSupported(true);
			shippingRate.setCountry(customer.getCountry());
			shippingRate.setState(state);
			shippingRate.setDays(2);
			shippingRate.setRate((float) 0.2);
			return shippingRate;
		}
		else {
			return repo.findByCountryAndState(customer.getCountry(), state);
		}
	}
	
	public ShippingRate getShippingRateForAddress(Address address) {
		String state = address.getState();
		if (state == null || state.isEmpty()) {
			state = address.getCity();
		}
		// Si la ubicacion es españa permite el envio con una tasa de 2€ por kilo y 2 dias de plazo de envio
		if (address.getCountry().getCode().equals("ES")) {
			ShippingRate shippingRate =  new ShippingRate();
			shippingRate.setCodSupported(true);
			shippingRate.setCountry(address.getCountry());
			shippingRate.setState(state);
			shippingRate.setDays(2);
			shippingRate.setRate((float) 0.2);
			return shippingRate;
		}
		else {
			return repo.findByCountryAndState(address.getCountry(), state);
		}
	}	
}
