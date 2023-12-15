package com.starq.orderservice.service;


import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.starq.orderservice.dto.OrderLineItemsDto;
import com.starq.orderservice.dto.OrderRequest;
import com.starq.orderservice.model.Order;
import com.starq.orderservice.model.OrderLineItems;
import com.starq.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItemsList =  orderRequest.getOrderLineItemsDtoList()
        .stream()
        .map(this::mapToDto)
        .toList();

        order.setOrderLineItemsList(orderLineItemsList);

        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());

        return orderLineItems;
    }
}