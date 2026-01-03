package org.restaurant.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // The waiter who placed the order

    private int tableNumber;

    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    private double totalAmount;
    private double discountAmount;

    public Order() {
        this.orderDate = LocalDateTime.now();
    }

    public void setUser(User user) { this.user = user; }
    public User getUser() { return this.user; }
    public void setTableNumber(int tableNumber) { this.tableNumber = tableNumber; }
    public int getTableNumber() { return tableNumber; }
    public LocalDateTime getOrderDate() { return orderDate; }

    public void addItem(Product product, int quantity) {
        items.add(new OrderItem(this, product, quantity));
    }

    public List<OrderItem> getItems() { return items; }

    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setDiscountAmount(double discountAmount) { this.discountAmount = discountAmount; }
    public double getTotalAmount() { return totalAmount; }
    public double getDiscountAmount() { return discountAmount; }
    public Long getId() { return id; }
}