package com.tiendadenacho.admin.customer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tiendadenacho.admin.paging.PagingAndSortingHelper;
import com.tiendadenacho.admin.paging.PagingAndSortingParam;
import com.tiendadenacho.entidades.Country;
import com.tiendadenacho.entidades.Customer;
import com.tiendadenacho.exception.CustomerNotFoundException;


@Controller
public class CustomerController {
	private String defaultRedirectURL = "redirect:/customers/page/1?sortField=firstName&sortDir=asc";
	
	@Autowired private CustomerService service;
	
	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}

	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listCustomers", moduleURL = "/customers") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {

		service.listByPage(pageNum, helper);
		
		return "customers/customers";
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		service.updateCustomerEnabledStatus(id, enabled);
		String status = enabled ? "activado" : "desactivado";
		String message = "El cliente con ID " + id + " ha sido " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/customers";
	}	
	
	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer customer = service.get(id);
			model.addAttribute("customer", customer);
			
			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/customers";			
		}
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Customer customer = service.get(id);
			List<Country> countries = service.listAllCountries();
			
			model.addAttribute("listCountries", countries);			
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", String.format("Editar cliente (ID: %d)", id));
			
			return "customers/customer_form";
			
		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/customers";
		}
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, Model model, RedirectAttributes ra) {
		service.save(customer);
		ra.addFlashAttribute("message", "El cliente con ID " + customer.getId() + " ha sido actualizado.");
		return "redirect:/customers";
	}

	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable Integer id, RedirectAttributes ra) {
		try {
			service.delete(id);			
			ra.addFlashAttribute("message", "El cliente con ID " + id + " Ha sido borrado.");
			
		} catch (CustomerNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/customers";
	}
	
}
