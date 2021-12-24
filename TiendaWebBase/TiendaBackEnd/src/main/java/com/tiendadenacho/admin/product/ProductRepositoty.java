package com.tiendadenacho.admin.product;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.transaction.annotation.Transactional;

import com.tiendadenacho.entidades.Product;

@Transactional
public interface ProductRepositoty extends PagingAndSortingRepository<Product, Integer> {
	
	public Product findByName (String name);
	
	
	@Query("UPDATE Product p SET p.enabled = ?2 WHERE p.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);	
	
	public Long countById (Integer id);

}
