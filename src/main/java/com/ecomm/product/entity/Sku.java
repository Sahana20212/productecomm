package com.ecomm.product.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="sku")
@Data
@AllArgsConstructor
@NoArgsConstructor


public class Sku {
	
	@Id
	
	@Column(name="sku_Id")
	private int skuId;
	
	@Column(name="sku_Name")
	private String skuName;
	
	@Column(name="color_id")
	private int colorId;
	
	@Transient
	private String colorName;
	
	@Column(name="size_Id")
	private int sizeID;
	@Transient
	private String sizeName;
	
	@Transient
	private double regularPrice;
	@Transient
	private double salePrice;
	@Transient
	private String inventory;
	
	
	

}