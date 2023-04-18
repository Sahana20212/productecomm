package com.ecomm.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecomm.product.entity.Brand;
import com.ecomm.product.entity.Color;
import com.ecomm.product.entity.Product;
import com.ecomm.product.entity.ProductSkuRel;
import com.ecomm.product.entity.Size;
import com.ecomm.product.entity.Sku;
import com.ecomm.product.entity.SkuInv;
import com.ecomm.product.entity.SkuPrice;
import com.ecomm.product.repository.ProductRepository;
import com.ecomm.product.repository.ProductSkuRelRepo;
import com.ecomm.product.repository.SkuInvRepository;
import com.ecomm.product.repository.SkuRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productrepository;
	@Autowired
	private SkuRepository skurepository;
	@Autowired
	private SkuInvRepository skuinvRepo;
	@Autowired
	private ProductSkuRelRepo productSkuRelRepo;

	SkuPrice skuprice = new SkuPrice();

	@Value("com.productecomm.brandservice")
	String getBrandbyNameHost;
	@Value("com.productecomm.getsizeservice")
	String getsizebyNameHost;
	@Value("com.productecomm.getcolorservice")
	String getcolorbyNameHost;

	public Product saveproduct(Product product) {
		// find brandId getbyName
		String brandId = fetchBrandId(product.getBrandName());
		product.setBrand_id(Long.parseLong(brandId));
		// save SKU Data
		product.setSkus(saveSkuData(product.getSkus(), product.getProduct_id()));
		// save sku prod relation
		productrepository.save(product);
		return product;
	}

	public List<Sku> saveSkuData(List<Sku> skus, long productId) {

		// aim is save sku ,price,inventory.
	 for(int i=0;i<skus.size();i++) {
		// step1 identify sizeid by size name, write a method and call it
		String sizeId = findSizeId(skus.get(i).getSizeName());
		Sku s1 = skus.get(i);
		s1.setSizeID((Integer.parseInt(sizeId)));

		// step2 identify color id by color name , write a method and invoke
		// find colorId getbyName
		String colorId = fetchColorId(skus.get(i).getColorName());

		s1.setColorId(Integer.parseInt(colorId));
		skurepository.save(s1);

		// step3: SKU price data, make rest call to insert sku price data using rest
		// template
		int skuId = s1.getSkuId();
		skuprice.setSku_id(skuId);
		skuprice.setRegularprice(s1.getRegularPrice());
		skuprice.setSaleprice(s1.getSalePrice());
		saveSkuPrice(skuprice);

		// step4: save inventory data
		SkuInv si = new SkuInv();
		si.setSku_id(s1.getSkuId());
		boolean inv = Boolean.parseBoolean(skus.get(i).getInventory());
		si.setInventory(inv);
		skuinvRepo.save(si);

		// step5 save sku product relation
		ProductSkuRel ps = new ProductSkuRel();
		ps.setProduct_id((int) productId);
		ps.setSku_id(skuId);
		productSkuRelRepo.save(ps);
	 }

		return skus;

	}

	private void saveSkuPrice(SkuPrice sp) {
		RestTemplate sizeVal = new RestTemplate();
		String url = "http://localhost:8085/skuprice";
		ResponseEntity<SkuPrice> result = sizeVal.postForEntity(url, sp, SkuPrice.class);

	}

	private String findSizeId(String sizeName) {
		String sizeId = "";

		// use rest template to size service
		RestTemplate sizeVal = new RestTemplate();
		Size response = new Size();
		String url = "http://localhost:8084/size/name/" + sizeName;
		// get sizeId by Name
		try {
			response = sizeVal.getForObject(url, Size.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response != null && (response.getId() != null)) {
			sizeId = response.getId();
		} else {
			url = "http://localhost:8084/size";
			Size s = new Size();
			// s.setId(sizeId);
			s.setName(sizeName);
			ResponseEntity<Size> result = sizeVal.postForEntity(url, s, Size.class);
			sizeId = result.getBody().getId();
			// insert new brand if not exists
		}
		return sizeId;

		// if sizeId exists return sizeId

		// else insert size record by making a post call using rest template

	}

	private String fetchColorId(String colorName) {
		String colorId = "";
		// make a service call to color service to fetch colorid using color name,,,this
		// is busineess logic
		RestTemplate colorVal = new RestTemplate();

		Color response = new Color();
		String url = "http://localhost:8082/color/name/" + colorName;
		try {
			response = colorVal.getForObject(url, Color.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response != null && (response.getId() != null)) {
			colorId = response.getId();
		} else {
			url = "http://localhost:8082/color";
			Color c1 = new Color();
			// b.setId(brandId);
			c1.setName(colorName);
			ResponseEntity<Color> result = colorVal.postForEntity(url, c1, Color.class);
			colorId = result.getBody().getId();
			// insert new brand if not exists
		}
		return colorId;
	}

	private String fetchBrandId(String brandName) {
		String brandId = "";
		// make a service call to brand service to fetch brandid using brand name,,,this
		// is busineess logic
		RestTemplate brandVal = new RestTemplate();

		Brand response = new Brand();
		String url = getBrandbyNameHost + brandName;
		try {
			response = brandVal.getForObject(url, Brand.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (response != null && (response.getId() != null)) {
			brandId = response.getId();
		} else {
			url = "http://localhost:8081/brand";
			Brand b = new Brand();
			// b.setId(brandId);
			b.setName(brandName);
			ResponseEntity<Brand> result = brandVal.postForEntity(url, b, Brand.class);
			brandId = result.getBody().getId();
			// insert new brand if not exists
		}
		return brandId;
	}

}