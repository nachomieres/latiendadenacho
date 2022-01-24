package com.tiendadenacho.admin.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.tiendadenacho.entidades.Brand;
import com.tiendadenacho.entidades.Category;
import com.tiendadenacho.entidades.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateProduct () {
		Brand brand = entityManager.find (Brand.class, 1);
		Category category = entityManager.find(Category.class, 4);
		
		Product product = new Product();
		
		product.setName("Acer Aspire XZ");
		product.setAlias("acer_aspire_xz");
		product.setShortDescription("Descripcion corta de Acer Aspire XZ");
		product.setFullDescription("Descripcion larga de Acer Aspire XZ");
		
		product.setEnabled(true);
		product.setInStock(true);
		product.setBrand(brand);
		product.setCategory(category);
		
		product.setPrice(890);
		product.setCreatedTime(new Date ());
		product.setUpdatedTime(new Date ());
		
		Product savedProduct = repo.save(product);
		assertThat(savedProduct).isNotNull();
		assertThat(savedProduct.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllProducts() {
		Iterable<Product> iterableProducts = repo.findAll();
		
		iterableProducts.forEach(System.out::println);
	}
	
	@Test
	public void testGetProduct() {
		Integer id = 1;
		Product product = repo.findById(id).get();
		System.out.println(product);
		
		assertThat(product).isNotNull();
	}
	
}
