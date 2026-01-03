package org.restaurant.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    private double priceAtOrder;

    public OrderItem() {}

    public OrderItem(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.priceAtOrder = product.getPrice();
    }

    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public double getPriceAtOrder() { return priceAtOrder; }

    public double getTotalPrice() {
        return priceAtOrder * quantity;
    }
}
