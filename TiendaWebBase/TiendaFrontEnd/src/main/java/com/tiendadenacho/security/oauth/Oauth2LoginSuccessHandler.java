package com.tiendadenacho.security.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.tiendadenacho.customer.CustomerService;
import com.tiendadenacho.entidades.AuthenticationType;
import com.tiendadenacho.entidades.Customer;

@Component
public class Oauth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired private CustomerService customerService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		CustomerOauthUser oauth2User = (CustomerOauthUser) authentication.getPrincipal();
		
		String name = oauth2User.getName();
		String email = oauth2User.getEmail();
		String countryCode = request.getLocale().getCountry();
		
		System.out.println("OAuth2LoginSuccessHandler: " + name + " " + email);
		
		Customer customer = customerService.getCustomerByEmail(email);
		
		if (customer == null) {
			customerService.addNewCustomerUponOAuthLogin (name, email, countryCode);
		}
		else {
			customerService.updateAuthenticationType(customer, AuthenticationType.GOOGLE);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
