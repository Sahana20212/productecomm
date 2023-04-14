package com.ecomm.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecomm.product.entity.Brand;
import com.ecomm.product.entity.Product;
import com.ecomm.product.entity.Size;
import com.ecomm.product.entity.Sku;
import com.ecomm.product.repository.ProductRepository;


@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productrepository;
	@Value("com.productecomm.brandservice")
	String getBrandbyNameHost;
	@Value("com.productecomm.getsizeservice")
	String getsizebyNameHost;
	
	public Product saveproduct(Product product) {
		//find brandId getbyName
		 String brandId=fetchBrandId(product.getBrandName());
		 product.setBrand_id(Long.parseLong(brandId));
		 // save SKU Data
		 saveSkuData(product.getSkus());
		 // save sku prod relation
		 
		return productrepository.save(product);
	}
	
	public Sku saveSkuData(List<Sku> skus){
		
		//aim is save sku ,price,inventory.

		//step1 identify sizeid by size name, write a method and call it
		String sizeId=findSizeId( skus.get(0).getSizeName());
		Sku s1=skus.get(0);
		s1.setSizeID((sizeId));
		
		
		//step2 identify color id by color name , write a method and invoke 
		
		//step3: SKU price data, make rest call to insert sku price data using rest template
		
		//step4: save inventory data
		
		
		return null;
		
	}
	
	private String findSizeId(String sizeName) {
		String sizeId="";
		
		//use rest template to size service
		RestTemplate sizeVal=new  RestTemplate();
		Size response=new Size();
		String url
		  = getsizebyNameHost +sizeName;
		//get sizeId by Name
		try {
			 response
			  = sizeVal.getForObject(url, Size.class);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			if(response!=null&& (response.getSizeId()!=null)) {
				sizeId=response.getSizeId();
			}else {
				url="http://localhost:8084/size";
				Size s= new Size();
				//s.setId(sizeId);
				s.setSizeName(sizeName);
				ResponseEntity<Size> result=sizeVal.postForEntity(url, s, Size.class);
				sizeId=result.getBody().getSizeId();
				//insert new brand if not exists
			}
			return sizeId;
		
		
		//if sizeId exists return sizeId
		
		//else insert size record by making a post call using rest template
		
		
	}

	private String fetchBrandId(String brandName) {
		String brandId="";
		// make a service call to brand service to fetch brandid using brand name,,,this is  busineess logic
		RestTemplate brandVal=new  RestTemplate();

		Brand response=new Brand();
		String url
		  = getBrandbyNameHost +brandName;
		try {
		 response
		  = brandVal.getForObject(url, Brand.class);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if(response!=null&& (response.getId()!=null)) {
			brandId=response.getId();
		}else {
			url="http://localhost:8081/brand";
			Brand b= new Brand();
			//b.setId(brandId);
			b.setName(brandName);
			ResponseEntity<Brand> result=brandVal.postForEntity(url, b, Brand.class);
			brandId=result.getBody().getId();
			//insert new brand if not exists
		}
		return brandId;
	}

}