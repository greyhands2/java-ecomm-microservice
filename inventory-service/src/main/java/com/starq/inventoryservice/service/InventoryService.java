package com.starq.inventoryservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starq.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    
    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
        //we are going to create this custom extension method in the in our repository
       return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }
}