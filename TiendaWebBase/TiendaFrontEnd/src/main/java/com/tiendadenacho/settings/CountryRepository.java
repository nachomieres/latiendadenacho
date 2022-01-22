package com.tiendadenacho.settings;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tiendadenacho.entidades.Country;


public interface CountryRepository extends CrudRepository<Country, Integer> {
	public List<Country> findAllByOrderByNameAsc();
}