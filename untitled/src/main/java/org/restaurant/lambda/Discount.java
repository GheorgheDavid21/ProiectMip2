package org.restaurant.lambda;

import org.restaurant.model.Order;

public interface Discount {
    public double applyDiscount(Order order, double discountRate);
}

