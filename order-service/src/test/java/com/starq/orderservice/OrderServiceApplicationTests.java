package com.starq.orderservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.starq.orderservice.dto.OrderLineItemsDto;
import com.starq.orderservice.dto.OrderRequest;
import com.starq.orderservice.model.Order;
import com.starq.orderservice.model.OrderLineItems;
import com.starq.orderservice.repository.OrderRepository;



@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class OrderServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OrderRepository orderRepository;

	@Container
	static MySQLContainer mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:latest"));


	@DynamicPropertySource
	static void configTestProps(DynamicPropertyRegistry registry){
		registry.add("spring.datasource.url", () -> mySQLContainer.getJdbcUrl());
        registry.add("spring.datasource.driverClassName", () -> mySQLContainer.getDriverClassName());
        registry.add("spring.datasource.username", () -> mySQLContainer.getUsername());
        registry.add("spring.datasource.password", () -> mySQLContainer.getPassword());
		registry.add("spring.jpa.hibernate.ddl-auto",() -> "create");
        registry.add("spring.flyway.enabled", () -> "true");
	}

	@BeforeEach
	public void beforeEach(){
		//create default orders
		OrderLineItems order1 =  OrderLineItems.builder()
		.skuCode("macbook-m1-air")
		.price(BigDecimal.valueOf(1000000))
		.quantity(Integer.valueOf(2))
		.build();

		OrderLineItems order2 =  OrderLineItems.builder()
		.skuCode("apple-ipad")
		.price(BigDecimal.valueOf(600000))
		.quantity(Integer.valueOf(1))
		.build();

		List<OrderLineItems> orderList = new ArrayList<>();
		orderList.add(order1);
		orderList.add(order2);

		Order newOrderRequest =  Order.builder()
		.orderLineItemsList(orderList)
		.build();

		orderRepository.save(newOrderRequest);
	}

	@AfterEach
	public void afterEach(){
		orderRepository.deleteAll();
	}



	@Test
	void shouldCreateOrders() {

		try {
			OrderLineItemsDto orderReq1 = OrderLineItemsDto.builder()
			.skuCode("samsung-s23-ultra")
			.price(BigDecimal.valueOf(1200000))
			.quantity(Integer.valueOf(1))
			.build();


			OrderLineItemsDto orderReq2 = OrderLineItemsDto.builder()
			.skuCode("macbook-m1-air")
			.price(BigDecimal.valueOf(1000000))
			.quantity(Integer.valueOf(2))
			.build();

			List<OrderLineItemsDto> orderReqList = new ArrayList<>();
			orderReqList.add(orderReq1);
			orderReqList.add(orderReq2);

			OrderRequest newOrderReq =  OrderRequest.builder()
			.orderLineItemsDtoList(orderReqList)
			.build();

			String orderRequestString = objectMapper.writeValueAsString(newOrderReq);
			mockMvc.perform(post("/api/order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(orderRequestString))
				.andExpect(status().isCreated());
				//after successfully adding this order we should have two orders in the order list which should contain a total of 4 items i.e 2 per order so our assertion should equal 2

				assertEquals(2, orderRepository.findAll().size());
		} catch(Exception e) {
			fail("Exception occurred during creation of order: " + e.getMessage());
		}
		
	}

}
