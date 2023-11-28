package com.starq.productservice.service;

import org.springframework.stereotype.Service;

import com.starq.productservice.dto.ProductRequest;
import com.starq.productservice.model.Product;
import com.starq.productservice.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
        .name(productRequest.getName())
        .description(productRequest.getDescription())
        .price(productRequest.getPrice())
        .build();

    productRepository.save(product);
    log.info("Product {} is saved", product.getId());

    }
}