package org.restaurant.lambda;

import org.restaurant.Order;

public interface Discount {
    public double applyDiscount(Order order, double discountRate);
}

