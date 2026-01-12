package org.restaurant.view;

import javafx.beans.property.*;
import org.restaurant.model.Order;
import org.restaurant.model.OrderItem;

public class OrderItemView {
    private final OrderItem orderItem;

    private final StringProperty productName = new SimpleStringProperty();
    private IntegerProperty quantity = new SimpleIntegerProperty();
    private final DoubleProperty priceAtOrder = new SimpleDoubleProperty();
    public OrderItemView(OrderItem orderItem) {
        this.orderItem = orderItem;
        this.productName.set(orderItem.getProduct().getName());
        this.quantity.set(orderItem.getQuantity());
        this.priceAtOrder.set(orderItem.getPriceAtOrder());
    }
    public OrderItem getOrderItem() {
        return orderItem;
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getPriceAtOrder() {
        return priceAtOrder.get() * quantity.get();
    }

    public DoubleProperty priceAtOrderProperty() {
        priceAtOrder.set(priceAtOrder.get() * quantity.get());
        return priceAtOrder;
    }

    public void updateView() {
        this.quantity.set(orderItem.getQuantity());
    }

//    public String getTotalPrice() {
//        double total = this.priceAtOrder * this.quantity;
//        return String.format("%.2f RON", total);
//    }
}
