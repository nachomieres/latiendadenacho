package com.tiendadenacho.admin.customer;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tiendadenacho.admin.paging.PagingAndSortingHelper;
import com.tiendadenacho.admin.setting.country.CountryRepository;
import com.tiendadenacho.entidades.Country;
import com.tiendadenacho.entidades.Customer;
import com.tiendadenacho.exception.CustomerNotFoundException;



@Service
@Transactional
public class CustomerService {
	public static final int CUSTOMERS_PER_PAGE = 10;
	
	@Autowired private CustomerRepository customerRepo;
	@Autowired private CountryRepository countryRepo;	
	@Autowired private PasswordEncoder passwordEncoder;
	
	public void listByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, CUSTOMERS_PER_PAGE, customerRepo);
	}
	
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}
	
	public Customer get(Integer id) throws CustomerNotFoundException {
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new CustomerNotFoundException("No se encuentra al cliente con ID " + id);
		}
	}

	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}		
	
	public boolean isEmailUnique(Integer id, String email) {
		Customer existCustomer = customerRepo.findByEmail(email);

		if (existCustomer != null && existCustomer.getId() != id) {
			// encontrado otro cliente con ese email
			return false;
		}
		
		return true;
	}
	
	public void save(Customer customerInForm) {
		Customer customerInDB = customerRepo.findById(customerInForm.getId()).get();
		
		if (!customerInForm.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
			customerInForm.setPassword(encodedPassword);			
		} else {
			customerInForm.setPassword(customerInDB.getPassword());
		}		
		
		customerInForm.setEnabled(customerInDB.isEnabled());
		customerInForm.setCreatedTime(customerInDB.getCreatedTime());
		customerInForm.setVerificationCode(customerInDB.getVerificationCode());
		
		customerRepo.save(customerInForm);
	}
	
	public void delete(Integer id) throws CustomerNotFoundException {
		Long count = customerRepo.countById(id);
		if (count == null || count == 0) {
			throw new CustomerNotFoundException("No se encuentra al cliente con ID " + id);
		}
		
		customerRepo.deleteById(id);
	}
	
}
