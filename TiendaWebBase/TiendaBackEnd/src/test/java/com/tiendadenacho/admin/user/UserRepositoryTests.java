package com.tiendadenacho.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.tiendadenacho.entidades.Role;
import com.tiendadenacho.entidades.User;

@DataJpaTest
@AutoConfigureTestDatabase (replace = Replace.NONE)
@Rollback (false)

public class UserRepositoryTests {
	
	@Autowired private UserRepository repo;
	
	@Autowired private TestEntityManager entityManager;
	
	@Test
	public void testCreateUserAdmin () {
		Role roleAdmin = entityManager.find(Role.class, 1);
		// Password nacho2022
		User nacho = new User ("admin@tdn.es", "$2a$10$aX3esB0ZHm8W82/oMagj/eTKWNafi6gVt5VFqwSgthmTVsCHz51hu", "Nacho", "Gonzalez");
		nacho.addRole(roleAdmin);
		
		repo.updateEnabledStatus(nacho.getId(), true);
		
		User savedUser = repo.save(nacho);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

}
