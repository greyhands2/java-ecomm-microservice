package com.starq.productservice;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.assertions.Assertions;

import com.starq.productservice.dto.ProductRequest;
import com.starq.productservice.repository.ProductRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo"); 

	

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}
	

	@Test
	void shouldCreateAndFetchProducts() {
		CompletableFuture<Void> result = createProduct().thenComposeAsync(created -> {
			return CompletableFuture.runAsync(()->{
				try {
					mockMvc.perform(get("/api/product")
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$.length()").value(productRepository.findAll().size()));
				} catch(Exception e){
					fail("Exception occurred during product fetch: " + e.getMessage());
				}
				
			});
		});

		


	result.join();	
			
		
	}
	 
	
	private CompletableFuture<Boolean> createProduct() {
		
			return CompletableFuture.supplyAsync(()->{
				try {
					ProductRequest productRequest = getProductRequest();
					//let's do an equivalent of JSON.stringify in java
					String productRequestString = objectMapper.writeValueAsString(productRequest);

					mockMvc.perform(post("/api/product")
					.contentType(MediaType.APPLICATION_JSON)
					.content(productRequestString))
					.andExpect(status().isCreated());
					assertEquals(1, productRepository.findAll().size());
					return true;
				} catch(Exception e){
					return false;
				}
					

				
			});
			
		

	}

	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
		.name("Google Pixel")
		.description("Google's answer to Iphone")
		.price(BigDecimal.valueOf(1100))
		.build();
	}

}
