package com.main.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.main.model.Invoice;

@Service
public class DataService {
	public Context setData(Invoice invoice) {
		Context context = new Context();
		Map<String, Object> map = new HashMap<>();
		map.put("invoice", invoice); // Use "invoice" instead of "users"
		context.setVariables(map);
		return context;
	}
}
