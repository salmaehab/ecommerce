package com.eg.swa.ntier.shopping.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.eg.swa.ntier.shopping.dto.ProductDto;
import com.eg.swa.ntier.shopping.model.*;
import com.eg.swa.ntier.shopping.repository.*;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final CustomerRepository customerRepository;
	private final ShoppingCartRepository cartRepository;
	private final ProductService productService;
	private final OrderItemRepository orderItemRepository;

	public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CustomerRepository customerRepository, ShoppingCartRepository cartRepository,
                        ProductService productService, OrderItemRepository orderItemRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.orderItemRepository = orderItemRepository;
    }

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public List<Order> getOrdersForCustomer(Customer customer) {
		return orderRepository.findByCustomer(customer);
	}

	public Order getOrderById(Long id) throws NotFoundException {
		return orderRepository.findById(id).orElseThrow(() -> new NotFoundException());
	}

	@Transactional
	public Order createOrder(Long customerId) throws Exception {

		// Need to refacator the following function
//		Order order = new Order();
//
//		Customer customer = new Customer();
//		customer.setId(customerId);
//
//		order.setCustomer(customer);
//		order.setOrderDate(LocalDateTime.now());
//		order.setOrderStatus(OrderStatus.CREATED);
//
//		List<OrderItem> items = new ArrayList<>();
//		for (OrderItemDto itemDto : orderItems) {
//
//			Product product = productRepository.getReferenceById(itemDto.getProductId());
//
//			// check if product is available in sufficient quantity
//			if (product.getQuantity() < itemDto.getQuantity()) {
//				throw new Exception("Insufficient Product Quantity");
//			}
//
//			// update product quantity
//			product.setQuantity(product.getQuantity() - itemDto.getQuantity());
////			productService.updateProduct(product.getId(), product); need to add the update product
//
//			// Create order item and add to items list
//			OrderItem item = new OrderItem();
//			item.setOrder(order);
//			item.setProduct(product);
//			item.setQuantity(itemDto.getQuantity());
//			item.setPrice(itemDto.getPrice());
//			items.add(item);
//		}
//		order.setOrderItems(items);
//		return orderRepository.save(order);

		Order order = new Order();
		Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
		if (optionalCustomer.isEmpty()) {
			throw new Exception("Customer not found");
		}
		Customer customer = optionalCustomer.get();
		order.setCustomer(customer);
		order.setOrderDate(LocalDateTime.now());
		order.setOrderStatus(OrderStatus.CREATED);
		Optional<ShoppingCart> optionalCart = cartRepository.findByCustomerId(customerId);
        List<OrderItem> items = new ArrayList<>();
		BigDecimal total = BigDecimal.valueOf(0);
		for (CartItem itemDto : optionalCart.get().getCartItems()) {

			Product product = productRepository.getReferenceById(itemDto.getProduct().getId());

			// check if product is available in sufficient quantity
			if (product.getQuantity() < itemDto.getQuantity()) {
				throw new Exception("Insufficient Product Quantity");
			}

			// update product quantity
			product.setQuantity(product.getQuantity() - itemDto.getQuantity());
			ProductDto productDto = new ProductDto();
			productDto.setId(product.getId());
			productDto.setName(product.getName());
			productDto.setQuantity(product.getQuantity());
			productDto.setPrice(product.getPrice());
			productService.updateProduct(productDto);

			// Create order item and add to items list
			OrderItem item = new OrderItem();
			item.setOrder(order);
			item.setProduct(product);
			item.setQuantity(itemDto.getQuantity());
			item.setPrice(itemDto.getProduct().getPrice());

			items.add(item);
			total= itemDto.getProduct().getPrice().multiply(BigDecimal.valueOf(itemDto.getProduct().getQuantity())).add(total);

		}
		order.setTotal(total);
		order.setOrderItems(items);
		orderItemRepository.saveAll(items);
		cartRepository.deleteById(optionalCart.get().getId());
		return orderRepository.save(order);

	}
	public void deleteOrder(Long orderId) throws Exception {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if (optionalOrder.isEmpty()) {
			throw new Exception("Order not found");
		}
		List<OrderItem> items= orderItemRepository.findAllByOrderId(orderId);
		orderItemRepository.deleteAll(items);
		orderRepository.deleteById(optionalOrder.get().getId());
	}
	public void cancelOrder(Long orderId) throws Exception {
		Optional<Order> optionalOrder = orderRepository.findById(orderId);
		if (optionalOrder.isEmpty()) {
			throw new Exception("Order not found");
		}
		Order order = optionalOrder.get();
		order.setOrderStatus(OrderStatus.CANCELLED);
		orderRepository.save(order);
	}



}

