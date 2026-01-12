//package org.restaurant.service;
//
//import org.restaurant.model.Order;
//import org.restaurant.model.OrderItem;
//import org.restaurant.model.User;
//import org.restaurant.persistence.OrderRepository;
//
//import java.util.List;
//
//public class OrderManagementService {
//    private final OrderRepository orderRepository;
//    private final DiscountService discountService;
//
//    public OrderManagementService(OrderRepository orderRepository, DiscountService discountService) {
//        this.orderRepository = orderRepository;
//        this.discountService = discountService;
//    }
//    public Order placeOrder(User waiter, int tableNumber, List<OrderItem> cartItems){
//        if (cartItems == null || cartItems.isEmpty()){
//            throw new IllegalArgumentException("Cannot place an empty order.");
//        }
//
//        Order order = new Order();
//        order.setUser(waiter);
//        order.setTableNumber(tableNumber);
//
//        double subTotal = 0.0;
//        for(OrderItem item : cartItems){
//            order.addItem(item.getProduct(), item.getQuantity());
//            subTotal += item.getTotalPrice();
//        }
//
//        double discountConfigured = discountService.calculateDiscount(order.getItems());
//
//        order.setTotalAmount(subTotal - discountConfigured);
//        order.setDiscountAmount(discountConfigured);
//
//        orderRepository.save(order);
//
//        return order;
//    }
//}
