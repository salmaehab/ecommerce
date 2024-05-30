package com.eg.swa.ntier.shopping.controller;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.eg.swa.ntier.shopping.model.Order;
import com.eg.swa.ntier.shopping.model.OrderItemDto;
import com.eg.swa.ntier.shopping.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) throws NotFoundException {
        return orderService.getOrderById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Order> createOrder(@RequestParam Long customerId) throws Exception {
        Order order = orderService.createOrder(customerId);
        return new ResponseEntity<>( HttpStatus.CREATED);
    }



    @PostMapping("/cancel/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long id) throws Exception {
        orderService.cancelOrder(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }


    @PostMapping("/delete/{id}")
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) throws Exception {
         orderService.deleteOrder(id);
        return new ResponseEntity<>( HttpStatus.OK);
    }


    
}

