package com.ecomm.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecomm.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>{

}
