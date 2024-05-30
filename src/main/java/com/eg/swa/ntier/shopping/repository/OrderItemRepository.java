package com.eg.swa.ntier.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.eg.swa.ntier.shopping.model.OrderItem;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select oi from OrderItem  oi where oi.order.id=:orderId")
    List<OrderItem> findAllByOrderId(Long orderId);
}
