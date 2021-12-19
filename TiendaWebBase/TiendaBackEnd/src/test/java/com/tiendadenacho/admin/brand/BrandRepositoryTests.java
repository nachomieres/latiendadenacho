package com.tiendadenacho.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.tiendadenacho.entidades.Brand;
import com.tiendadenacho.entidades.Category;

@DataJpaTest (showSql = false)
@AutoConfigureTestDatabase (replace = Replace.NONE)
@Rollback (false)
public class BrandRepositoryTests {
	
	@Autowired
	private BrandRepository repo;
	
	@Test
	public void testCreateBrand () {
		Category portatiles = new Category(4);
		Brand acer = new Brand("Acer");
		acer.getCategories().add(portatiles);
		
		Brand savedBrand = repo.save(acer);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
		
	}

}
