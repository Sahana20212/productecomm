package com.ecomm.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.product.entity.Product;
import com.ecomm.product.service.ProductService;


@RestController
public class ProductController {
	@Autowired
	private ProductService productservice;
	
	@PostMapping("/product")
	public Product saveproduct(@RequestBody Product product) {
		
		return productservice.saveproduct(product);
	}

}
