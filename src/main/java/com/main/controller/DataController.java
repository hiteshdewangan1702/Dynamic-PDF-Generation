package com.main.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.main.model.Invoice;
import com.main.service.DataService;
import com.main.service.PdfGenerator;

@RestController
public class DataController {

	@Autowired
	private SpringTemplateEngine engine;

	@Autowired
	private DataService service;

	@Autowired
	private PdfGenerator generator;

	@PostMapping("/generate")
	public ResponseEntity<FileSystemResource> generatePdf(@RequestBody Invoice invoice) {
		// Generate the HTML from the Invoice data using Thymeleaf
		Context data = service.setData(invoice);
		String finalHtml = engine.process("index", data);

		// Generate a unique identifier based on the invoice data (could be JSON string)
		String invoiceData = invoice.toString(); // You could use a more structured format

		// Generate or retrieve the PDF
		String pdfPath = generator.htmlToPdf(finalHtml, invoiceData);

		if (pdfPath != null) {
			// Prepare the PDF for download
			File pdfFile = new File(pdfPath);
			FileSystemResource fileSystemResource = new FileSystemResource(pdfFile);

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());

			return new ResponseEntity<>(fileSystemResource, headers, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
