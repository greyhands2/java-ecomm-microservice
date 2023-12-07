package com.starq.productservice.service;

import java.util.List;
import java.util.stream.Stream;


import org.springframework.stereotype.Service;

import com.starq.productservice.dto.ProductRequest;
import com.starq.productservice.dto.ProductResponse;
import com.starq.productservice.model.Product;
import com.starq.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor //this creates a constructor and sets the private variables
@Slf4j
public class ProductService {
    
    private final ProductRepository productRepository;

    // public ProductService(ProductRepository productRepository){
    //     this.productRepository = productRepository;
    // }
    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .price(productRequest.getPrice())
        .build();

    productRepository.save(product);
    log.info("Product {} is saved", product.getId());

    }


    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();

        //map products of type Product to type ProductResponse
        //Stream<Product> product = products.stream();

        // return product.map(elem -> mapToProductResponse(elem)).toList();
        //or
        return products.stream()
        .map(this::mapToProductResponse)
        .toList();

 
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
        .id(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .build();
    }
}