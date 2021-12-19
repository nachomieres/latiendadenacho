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
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	/*
	@Test
	public void testCreateUserWithOneRole () {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User nacho = new User ("nachomieres@gmail.com", "nacho123", "Nacho", "Gonzalez");
		nacho.addRole(roleAdmin);
		
		User savedUser = repo.save(nacho);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateUserWithTwoRoles () {
		 User cris = new User("crisfdemon@gmail.com", "cris123", "Cris", "Montañes");
		 Role roleEditor = new Role (3);
		 Role roleAsistente = new Role (5);
		 cris.addRole(roleEditor);
		 cris.addRole(roleAsistente);
		 
		 User savedUser = repo.save(cris);
		 assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUser () {
		Iterable<User> listUsers = repo.findAll();
		
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById () {
		User userCris = repo.findById(2).get();
		System.out.println(userCris);
		assertThat(userCris).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails () {
		User userCris = repo.findById(2).get();
		userCris.setEnabled(true);
		userCris.setEmail("cris@gmail.com");
		repo.save(userCris);	
	}
	
	@Test
	public void testUpdateRoles () {
		User userCris = repo.findById(2).get();
		Role roleEditor = new Role (3);
		Role roleGestor = new Role (2);
		userCris.getRoles().remove(roleEditor);
		userCris.addRole(roleGestor);
		
		repo.save(userCris);
	}
	
	@Test
	public void testDeleteUser () {
		Integer userId = 2;
		repo.deleteById(userId);
	}
	
	@Test
	public void testGetUserByEmail () {
		String email = "nachomieres@gmail.com";
		User user = repo.getUserByEmail(email);
		assertThat(user).isNotNull();
	}*/
}
