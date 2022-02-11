package com.tiendadenacho.admin.user;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tiendadenacho.admin.AbstractExporter;
import com.tiendadenacho.entidades.User;


public class UserPdfExporter extends AbstractExporter {
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, "application/pdf", ".pdf", "usuarios_");
		
		Document document = new Document(PageSize.A4.rotate());
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLACK);
		
		Paragraph paragraph = new Paragraph("Lista de Usuarios", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		
		document.add(paragraph);
		
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.2f, 3.0f, 3.0f, 4.0f, 2.5f, 1.7f});
		
		writeTableHeader(table);
		writeTableData(table, listUsers);
		
		document.add(table);
		
		document.close();
		
	}

	private void writeTableData(PdfPTable table, List<User> listUsers) {
		for (User user : listUsers) {
			table.addCell(String.valueOf(user.getId()));			
			table.addCell(user.getNombre());
			table.addCell(user.getApellidos());
			table.addCell(user.getEmail());
			table.addCell(user.getRoles().toString());
			table.addCell(String.valueOf(user.isEnabled()));
		}
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLACK);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);		
		
		cell.setPhrase(new Phrase("ID", font));		
		table.addCell(cell);			
		
		cell.setPhrase(new Phrase("Nombre", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Apellidos", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("E-mail", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Roles ", font));		
		table.addCell(cell);
		
		cell.setPhrase(new Phrase("Activo", font));		
		table.addCell(cell);		
	}
}
