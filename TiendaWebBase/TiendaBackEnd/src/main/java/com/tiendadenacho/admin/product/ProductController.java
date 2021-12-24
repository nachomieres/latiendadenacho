package com.tiendadenacho.admin.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tiendadenacho.admin.brand.BrandService;
import com.tiendadenacho.entidades.Brand;
import com.tiendadenacho.entidades.Product;

@Controller
public class ProductController {
	
	@Autowired private ProductService productService;
	@Autowired private BrandService brandService;
	
	@GetMapping ("/products")
	public String listAll (Model model) {
		List<Product> listProducts = productService.listAll();
		
		model.addAttribute("listProducts", listProducts);
		return "products/products";
	}
	
	@GetMapping ("/products/new")
	public String newProduct (Model model) {
		
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Crear nuevo producto");
		
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttributes) {
		productService.save(product);
		
		redirectAttributes.addFlashAttribute("message", "El producto se ha creado correctamente.");
		
		return "redirect:/products";
	}
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "activado" : "desactivado";
		String message = "El Producto ID " + id + " ha sido " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/products";
	}

}
