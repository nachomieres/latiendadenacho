package com.tiendadenacho.shoppingcart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiendadenacho.Utility;
import com.tiendadenacho.customer.CustomerService;
import com.tiendadenacho.entidades.Customer;
import com.tiendadenacho.exception.CustomerNotFoundException;

@RestController
public class ShoppingCartRestController {
	@Autowired private ShoppingCartService cartService;
	@Autowired private CustomerService customerService;
	
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request) {
		
		try {
			Customer customer = getAuthenticatedCustomer(request);
			Integer updatedQuantity = cartService.addProduct(productId, quantity, customer);
			
			return updatedQuantity + " unidad(es) del producto se han añadido al carrito..";
		} catch (CustomerNotFoundException ex) {
			return "Tienes que iniciar sesion para añadir articulos al carrito.";
		} catch (ShoppingCartException ex) {
			return ex.getMessage();
		}
		
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) 
			throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("Cliente no autenticado");
		}
				
		return customerService.getCustomerByEmail(email);
	}
}
