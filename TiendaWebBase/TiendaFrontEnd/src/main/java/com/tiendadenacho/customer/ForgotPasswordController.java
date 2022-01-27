package com.tiendadenacho.customer;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tiendadenacho.Utility;
import com.tiendadenacho.entidades.Customer;
import com.tiendadenacho.exception.CustomerNotFoundException;
import com.tiendadenacho.settings.EmailSettingBag;
import com.tiendadenacho.settings.SettingService;



@Controller
public class ForgotPasswordController {
	@Autowired private CustomerService customerService;
	@Autowired private SettingService settingService;
	
	@GetMapping("/forgot_password")
	public String showRequestForm() {
		return "customer/forgot_password_form";
	}
	
	@PostMapping("/forgot_password")
	public String processRequestForm(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		try {
			String token = customerService.updateResetPasswordToken(email);
			String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
			sendEmail(link, email);
			
			model.addAttribute("message", "Hemos enviado un link a tu email."
					+ " Por favor, compruebalo.");
		} catch (CustomerNotFoundException e) {
			model.addAttribute("error", e.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "No se puede enviar el email");
		}
		
		return "customer/forgot_password_form";
	}
	
	private void sendEmail(String link, String email) 
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		
		String toAddress = email;
		String subject = "¿Has olvidado tu contraseña?";
		
		String content = "<p>Hola,</p>"
				+ "<p>Has solicitado reestablecer tu contrasña.</p>"
				+ "Pincha en el link siguiente para resetearla:</p>"
				+ "<p><a href=\"" + link + "\">Cambiar mi contraseña</a></p>"
				+ "<br>"
				+ "<p>Si no has hecho esta petición, puedes ignorar este mensaje y tu contraseña continuará igual.</p>";
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);		
		
		helper.setText(content, true);
		mailSender.send(message);
	}
	
	@GetMapping("/reset_password")
	public String showResetForm(@Param("token") String token, Model model) {
		Customer customer = customerService.getByResetPasswordToken(token);
		if (customer != null) {
			model.addAttribute("token", token);
		} else {
			model.addAttribute("pageTitle", "Token no valido");
			model.addAttribute("message", "Token no valido");
			return "message";
		}
		
		return "customer/reset_password_form";
	}
	
	@PostMapping("/reset_password")
	public String processResetForm(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		String password = request.getParameter("password");
		
		try {
			customerService.updatePassword(token, password);
			
			model.addAttribute("pageTitle", "Reestablece tu contraseña");
			model.addAttribute("title", "Reestablece tu contraseña");
			model.addAttribute("message", "Has cambiado correctamente tu contraseña.");
			
		} catch (CustomerNotFoundException e) {
			model.addAttribute("pageTitle", "Token no valido");
			model.addAttribute("message", e.getMessage());
		}	

		return "message";		
	}
	
}
