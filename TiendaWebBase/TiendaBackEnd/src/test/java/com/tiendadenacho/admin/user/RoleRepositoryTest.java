package com.tiendadenacho.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.tiendadenacho.entidades.Role;

@DataJpaTest
@AutoConfigureTestDatabase (replace = Replace.NONE)
@Rollback (false)

public class RoleRepositoryTest {

	@Autowired
	private RoleRepository repo;
	/*
	@Test
	public void testCreateFirstRole () {
		Role roleAdmin = new Role ("Admin", "Controla todo");
		Role savedRole = repo.save(roleAdmin);
		
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateRestRoles () {
		Role roleGestor = new Role ("Gestor", "Controla los precios y los clientes");
		Role roleEditor = new Role ("Editor", "Controla categorias, marcas, productos...");
		Role roleEnvios = new Role ("Enviador", "Controla los pedidos");
		Role RoleAsistente = new Role ("Asistente", "Controla las preguntas y las reseñas");
		
		repo.saveAll(List.of(roleGestor, roleEditor, roleEnvios, RoleAsistente));
		
	}*/
}
