package com.tiendadenacho.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.tiendadenacho.admin.paging.PagingAndSortingHelper;
import com.tiendadenacho.admin.paging.PagingAndSortingParam;
import com.tiendadenacho.entidades.Role;
import com.tiendadenacho.entidades.User;

@Controller
public class UserController {
	
	private String defaultRedirectURL = "redirect:/users/page/1?sortField=nombre&sortDir=asc";

	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String listFirstPage() {
		return defaultRedirectURL;
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listUsers", moduleURL = "/users") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum) {
		service.listByPage(pageNum, helper);		
		
		return "users/users";		
	}
	
	@GetMapping("/users/new")
	public String newUser (Model model) {
		List<Role> listRoles = service.listRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Crear nuevo usuario");
		
		return "users/users_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser (User user, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setFoto(fileName);
			User savedUser = service.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();		
			
			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
//			FileUploadUtil.cleanDir(uploadDir);
//			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);			
			
		} else {
			if (user.getFoto().isEmpty()) user.setFoto(null);
			service.save(user);
		}
		redirectAttributes.addFlashAttribute("message", "El usuario se ha guardado correctamente.");

		return getRedirectURLtoAffectedUser(user);
	}

	private String getRedirectURLtoAffectedUser(User user) {
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1/?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
	}
	
	@GetMapping ("/users/edit/{id}")
	public String editUser (@PathVariable (name="id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			User user = service.get (id);
			List<Role> listRoles = service.listRoles();
			
			model.addAttribute("user", user);
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", "Editar usuario (Id: " + user.getId() + ")");
			return "users/users_form";
		}
		catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
	}
	@GetMapping ("/users/delete/{id}")
	public String deleteUser (@PathVariable (name="id") Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			service.delete(id);			
			String userPhotosDir = "user-photos/" + id;
			AmazonS3Util.removeFolder(userPhotosDir);			
			redirectAttributes.addFlashAttribute("message", "El usuario Id " + id  +" ha sido borrado correctamente.");
		}
		catch (UserNotFoundException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnabledStatus(@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, 
			RedirectAttributes redirectAttributes) {
		
		service.updateUserEnabledStatus(id, enabled);
		
		String status = enabled ? "activado" : "desactivado";
		String message = "El usuario ID " + id + " ha sido " + status;
		redirectAttributes.addFlashAttribute("message", message);
		
		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
	}	
	
}
