package com.tiendadenacho;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.tiendadenacho.category.CategoryService;
import com.tiendadenacho.entidades.Category;

@Controller
public class MainController {

	@Autowired private CategoryService categoryService;
	
	@GetMapping("")
	public String vistaHomePage (Model model) {
		List<Category> listCategories = categoryService.listNoChildrenCategories();
		
		model.addAttribute("listCategories", listCategories);
		
		return "index";
	}
}
