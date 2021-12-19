package com.tiendadenacho.admin.brand;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.tiendadenacho.entidades.Brand;

public interface BrandRepository extends PagingAndSortingRepository<Brand, Integer> {
	
	public Long countById (Integer id);

}
