package com.tiendadenacho.admin.brand;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tiendadenacho.admin.AmazonS3Util;
import com.tiendadenacho.admin.category.CategoryService;
import com.tiendadenacho.admin.paging.PagingAndSortingHelper;
import com.tiendadenacho.admin.paging.PagingAndSortingParam;
import com.tiendadenacho.entidades.Brand;
import com.tiendadenacho.entidades.Category;

@Controller
public class BrandController {
	private String defaultRedirectURL = "redirect:/brands/page/1?sortField=name&sortDir=asc";
	
	@Autowired
	BrandService service;
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping("/brands")
	public String listFirstPage() {
		return defaultRedirectURL;
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listBrands", moduleURL = "/brands") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum
			) {
		service.listByPage(pageNum, helper);
		return "brands/brands";		
	}
	
	@GetMapping("/brands/new")
	public String newBrand(Model model) {
		
		
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("brand", new Brand());
		model.addAttribute("pageTitle", "Crear nueva marca");
		
		return "brands/brand_form";		
	}
	
	@PostMapping ("/brands/save")
	public String sabeBrand (Brand brand,
				@RequestParam("fileImage") MultipartFile multipartFile,
				RedirectAttributes redirectAttributes) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand savedBrand = service.save(brand);
			String uploadDir = "brand-logos/" + savedBrand.getId();
			
//			FileUploadUtil.cleanDir(uploadDir);
//			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);		
			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
		} else {
			service.save(brand);
		}
				
		redirectAttributes.addFlashAttribute("message", "La marca se ha guardado correctamente.");
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String editBrand(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Brand brand = service.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			
			model.addAttribute("brand", brand);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Editar marca (ID: " + id + ")");
			
			return "brands/brand_form";			
		} catch (BrandNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return "redirect:/brands";
		}
	}
	
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(@PathVariable(name = "id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);
			String brandDir = "brand-logos/" + id;
			//FileUploadUtil.removeDir(brandDir);
			AmazonS3Util.removeFolder(brandDir);
			
			redirectAttributes.addFlashAttribute("message", 
					"La marca con ID " + id + " se ha borrado correctamente");
		} catch (BrandNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/brands";
	}	
}
