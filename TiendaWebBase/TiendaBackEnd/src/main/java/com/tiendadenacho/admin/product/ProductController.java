package com.tiendadenacho.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tiendadenacho.admin.FileUploadUtil;
import com.tiendadenacho.admin.brand.BrandService;
import com.tiendadenacho.admin.category.CategoryService;
import com.tiendadenacho.admin.paging.PagingAndSortingHelper;
import com.tiendadenacho.admin.paging.PagingAndSortingParam;
import com.tiendadenacho.admin.security.TiendaUserDetails;
import com.tiendadenacho.entidades.Brand;
import com.tiendadenacho.entidades.Category;
import com.tiendadenacho.entidades.product.Product;
import com.tiendadenacho.exception.ProductNotFoundException;

@Controller
public class ProductController {	
	private String defaultRedirectURL = "redirect:/products/page/1?sortField=name&sortDir=asc&categoryId=0";
	
	@Autowired private ProductService productService;
	@Autowired private BrandService brandService;
	@Autowired private CategoryService categoryService;

	@GetMapping("/products")
	public String listFirstPage(Model model) {
		return defaultRedirectURL;
	}
	
	@GetMapping("/products/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listProducts", moduleURL = "/products") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum, Model model,
			Integer categoryId
			) {
		
		productService.listByPage(pageNum, helper, categoryId);
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		if (categoryId != null) model.addAttribute("categoryId", categoryId);
		model.addAttribute("listCategories", listCategories);
		
		return "products/products";		
	}

	@GetMapping("/products/new")
	public String newProduct(Model model) {

		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);			

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Crear nuevo producto");
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttributes,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
			@RequestParam(name = "detailIDs", required = false) String[] detailIDs,
			@RequestParam(name="detailNames", required = false) String[] detailNames,
			@RequestParam(name="detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIDs", required = false) String[] imageIDs,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal TiendaUserDetails loggedUser
			) throws IOException {

		if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
			if (loggedUser.hasRole("Gestor")) {
				productService.saveProductPrice(product);
				redirectAttributes.addFlashAttribute("message", "El producto se ha guardado correctamente.");		
				return defaultRedirectURL;
			}
		}
		
		ProductSaveHelper.setMainImage(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImages (imageIDs, imageNames, product);		
		ProductSaveHelper.setNewExtraImages (extraImageMultiparts, product);
		ProductSaveHelper.setProductDetails(detailIDs, detailNames, detailValues, product);

		Product savedProduct = productService.save(product);
		
		ProductSaveHelper.saveUploadedImages (mainImageMultipart, extraImageMultiparts, savedProduct);
		
		ProductSaveHelper.deleteExtraImagesWeredRemovedOnForm(product);
		
		redirectAttributes.addFlashAttribute("message", "El producto se ha guardado correctamente.");

		return defaultRedirectURL;
	}
	

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes) {
		productService.updateProductEnabledStatus(id, enabled);
		String status = enabled ? "activado" : "desactivado";
		String message = "El Producto ID " + id + " ha sido " + status;
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/products";
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			productService.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id;
			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productImagesDir);
			

			redirectAttributes.addFlashAttribute("message",
					"El producto con ID  " + id + " ha sido borrado correctamente");
		} catch (ProductNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}

		return "redirect:/products";
	}
	
	@GetMapping ("/products/edit/{id}")
	public String editProduct (@PathVariable ("id") Integer id, Model model,
			RedirectAttributes redirectAttributes, @AuthenticationPrincipal TiendaUserDetails loggedUser) {
		try {
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingImages = product.getImages().size();
			
			boolean isReadOnlyForGestor = false;
			if (!loggedUser.hasRole("Admin") && !loggedUser.hasRole("Editor")) {
				if (loggedUser.hasRole("Gestor")) {
					isReadOnlyForGestor = true;
				}
			}
			
			model.addAttribute("isReadOnlyForGestor", isReadOnlyForGestor);
			
			model.addAttribute("product", product);
			model.addAttribute("pageTitle", "Editar producto (ID: " + id + ")");
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingImages);
			
			return "products/product_form";
			
		} catch (ProductNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}
	
	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Product product = productService.get(id);			
			model.addAttribute("product", product);		
			
			return "products/product_detail_modal";
			
		} catch (ProductNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			
			return "redirect:/products";
		}
	}	

}
