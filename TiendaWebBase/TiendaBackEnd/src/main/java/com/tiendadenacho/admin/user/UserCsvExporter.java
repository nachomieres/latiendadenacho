package com.tiendadenacho.admin.user;

import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.tiendadenacho.admin.AbstractExporter;
import com.tiendadenacho.entidades.User;

public class UserCsvExporter extends AbstractExporter {

	public void export (List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "text/csv", ".csv", "usuarios_");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), 
				CsvPreference.STANDARD_PREFERENCE);
		
		String[] csvHeader = {"ID", "Nombre", "Apellidos", "E-mail", "Roles", "Activo"};
		String[] fieldMapping = {"id", "nombre", "apellidos", "email", "roles", "enabled"};
		
		csvWriter.writeHeader(csvHeader);
		
		for (User user : listUsers) {
			csvWriter.write(user, fieldMapping);
		}
		
		csvWriter.close();
	}
}
