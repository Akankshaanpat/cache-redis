package com.snp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.snp.entity.Product;
import com.snp.service.ProductServiceImpl;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
public class ProductController {

	@Autowired
	private ProductServiceImpl productServiceImpl;
	
	@GetMapping("/getall")
	public List<Product> getAll()
	{
		return productServiceImpl.getAll();
	}
	@PostMapping("/save")
	public ResponseEntity<Product> save(@RequestBody Product  product)
	{
		log.info("handling save controller request");
		return ResponseEntity.ok(productServiceImpl.save(product));
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<Product> findById(@PathVariable Long id)
	{
		log.info("handling find by id  from controller");
		return ResponseEntity.ok(productServiceImpl.findById(id));
	}
	
	@DeleteMapping("/del/{id}")
	public void deleteProduct(@PathVariable Long id)
	{
		
		productServiceImpl.deleteProduct(id);
	}
}
