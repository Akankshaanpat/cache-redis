package com.snp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.snp.entity.Product;
import com.snp.repository.ProductRepo;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ProductServiceImpl  {

	@Autowired
	private ProductRepo productRepo;
	
	
	@CachePut(value = "product",key="#product.id")// CachePut is used to update the cache with the latest result
	public Product save(Product product)
	{
		log.info("Adding Product to database : {}",product);
		return productRepo.save(product);
	}
	
	@Cacheable(value = "product",key = "#id")// is used to indicate that method result should be cached so that if this method is accessed more than once,
	// then the method will not get executed and instead of that the result will be returned form the cache
	public Product findById(Long id)
	{
		log.info("Fetching record based on id : {}",id);
		return productRepo.findById(id).get();
	}

	@CacheEvict(value = "product",allEntries = true)// CacheEvict is used to delete the result form the cache if the delete method is called then only the the result will be deleted
	public void deleteProduct(Long id) {
		// TODO Auto-generated method stub
		productRepo.deleteById(id);
		
	}
 
	@Cacheable(value = "product", key = "'all'")
	public List<Product> getAll() {
		// TODO Auto-generated method stub
		return productRepo.findAll();
	}
}
