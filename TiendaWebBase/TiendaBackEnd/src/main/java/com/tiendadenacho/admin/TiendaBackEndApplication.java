package com.tiendadenacho.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan ({"com.tiendadenacho.entidades", "com.tiendadenacho.admin.user"})

public class TiendaBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiendaBackEndApplication.class, args);
	}

}
