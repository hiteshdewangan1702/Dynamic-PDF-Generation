package com.main.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfWriter;

@Service
public class PdfGenerator {

	private static final String PDF_STORAGE_PATH = "C:/Users/Hitesh_Dewangan/Desktop/PDF/";

	// Method to hash the invoice data to create a unique filename
	private String generateHash(String input) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] hashBytes = md.digest(input.getBytes());
		StringBuilder sb = new StringBuilder();
		for (byte b : hashBytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	// Check if PDF exists, if not generate a new one
	public String htmlToPdf(String html, String invoiceData) {
		try {
			// Generate a unique hash based on the invoice data
			String fileName = generateHash(invoiceData) + ".pdf";
			String filePath = PDF_STORAGE_PATH + fileName;

			// Check if the file already exists
			File file = new File(filePath);
			if (file.exists()) {
				System.out.println("PDF already exists. Returning existing file: " + filePath);
				return filePath;
			}

			// Generate the PDF if it doesn't exist
			ByteArrayOutputStream bis = new ByteArrayOutputStream();
			PdfWriter writer = new PdfWriter(bis);
			ConverterProperties prop = new ConverterProperties();
			prop.setFontProvider(new DefaultFontProvider());
			HtmlConverter.convertToPdf(html, writer, prop);

			// Save the generated PDF to the local file system
			try (FileOutputStream fos = new FileOutputStream(filePath)) {
				bis.writeTo(fos);
				bis.flush();
			}
			return filePath; // Return the path of the newly generated file

		} catch (IOException | NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
