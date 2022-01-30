package com.tiendadenacho.shoppingcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tiendadenacho.entidades.CartItem;
import com.tiendadenacho.entidades.Customer;
import com.tiendadenacho.entidades.Product;

@Service
public class ShoppingCartService {

	@Autowired private CartItemRepository cartRepo;
	
	public Integer addProduct(Integer productId, Integer quantity, Customer customer) 
			throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		Product product = new Product(productId);
		
		CartItem cartItem = cartRepo.findByCustomerAndProduct(customer, product);
		
		if (cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;
			
			if (updatedQuantity > 5) {
				throw new ShoppingCartException("No puedes añadir " + quantity + " unidad(es)"
						+ " porque ya hay " + cartItem.getQuantity() + " unidad(es) "
						+ "en el carrito. La cantidad maxima permitida es 5.");
			}
		} else {
			cartItem = new CartItem();
			cartItem.setCustomer(customer);
			cartItem.setProduct(product);
		}
		
		cartItem.setQuantity(updatedQuantity);
		
		cartRepo.save(cartItem);
		
		return updatedQuantity;
	}
	
	public List<CartItem> listCartItems(Customer customer) {
		return cartRepo.findByCustomer(customer);
	}
}
