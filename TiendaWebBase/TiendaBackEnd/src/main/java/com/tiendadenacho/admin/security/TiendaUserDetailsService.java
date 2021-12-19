package com.tiendadenacho.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import com.tiendadenacho.admin.user.UserRepository;
import com.tiendadenacho.entidades.User;

public class TiendaUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepo.getUserByEmail(email);
		if (user != null) {
			return new TiendaUserDetails(user);
		}
		
		throw new UsernameNotFoundException("No se encuentra el usuario con email: " + email);
		
	}

}
