package com.ecomm.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomm.product.entity.Product;
import com.ecomm.product.repository.ProductRepository;


@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productrepository;
	
	public Product saveproduct(Product product) {
		return productrepository.save(product);
	}

}