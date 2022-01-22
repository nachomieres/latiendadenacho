package com.tiendadenacho.settings;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tiendadenacho.entidades.Country;
import com.tiendadenacho.entidades.State;

public interface StateRepository extends CrudRepository<State, Integer> {
	
	public List<State> findByCountryOrderByNameAsc(Country country);
}
