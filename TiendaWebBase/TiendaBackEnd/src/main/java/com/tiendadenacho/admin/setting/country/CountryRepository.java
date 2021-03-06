package com.tiendadenacho.admin.setting.country;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tiendadenacho.entidades.Country;


public interface CountryRepository extends CrudRepository<Country, Integer> {
	public List<Country> findAllByOrderByNameAsc();

	public List<Country> findAllByOrderByIdAsc();
}