package com.ecomm.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="product")

public class Product {
	@Id
	@Column(name="product_id")
	private long product_id;
	@Column(name="product_name")
	private String product_name;
	@Column(name="brand_id")
	private long brand_id;
	@Column(name="rating")
	private String rating;
	

}
