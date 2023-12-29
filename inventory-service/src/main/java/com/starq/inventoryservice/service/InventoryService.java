package com.starq.inventoryservice.service;

import com.starq.inventoryservice.dto.InventoryResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starq.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    
    @Transactional(readOnly = true)
    @SneakyThrows
    public List<InventoryResponse>  isInStock(List<String> skuCode){
        log.info("wait started");
        Thread.sleep(10000);
        log.info("wait ended");
        //we are going to create this custom extension method in the in our repository
       return inventoryRepository.findBySkuCodeIn(skuCode).stream()
               .map(inventory ->
                   InventoryResponse.builder()
                           .skuCode(inventory.getSkuCode())
                           .isInStock(inventory.getQuantity() > 0)
                           .build()
               ).toList();
    }
}