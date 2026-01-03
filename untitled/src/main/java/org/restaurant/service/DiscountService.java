package org.restaurant.service;

import org.restaurant.model.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DiscountService {

    private boolean happyHourEnabled = true;
    private boolean mealDealEnabled = true;
    private boolean partyPackEnabled = true;

    public void setHappyHourEnabled(boolean enabled) { this.happyHourEnabled = enabled; }
    public void setMealDealEnabled(boolean enabled) { this.mealDealEnabled = enabled; }
    public void setPartyPackEnabled(boolean enabled) { this.partyPackEnabled = enabled; }

    public double calculateDiscount(List<OrderItem> items) {
        double totalDiscount = 0.0;

        if (happyHourEnabled) {
            totalDiscount += applyHappyHour(items);
        }
        if (mealDealEnabled) {
            totalDiscount += applyMealDeal(items);
        }
        if (partyPackEnabled) {
            totalDiscount += applyPartyPack(items);
        }

        return totalDiscount;
    }

    // Happy Hour: Every 2nd drink is 50% off
    private double applyHappyHour(List<OrderItem> items) {
        List<Double> drinkPrices = new ArrayList<>();
        for (OrderItem item : items) {
            if (item.getProduct() instanceof Drink) {
                for (int i = 0; i < item.getQuantity(); i++) {
                    drinkPrices.add(item.getPriceAtOrder());
                }
            }
        }
        // Sort cheapest first to apply discount to them (standard practice) or logic as defined
        drinkPrices.sort(Comparator.naturalOrder());

        double discount = 0;
        int drinksToDiscount = drinkPrices.size() / 2;

        for (int i = 0; i < drinksToDiscount; i++) {
            discount += drinkPrices.get(i) * 0.5;
        }
        return discount;
    }

    // Meal Deal: If Pizza exists, cheapest dessert is 25% off
    private double applyMealDeal(List<OrderItem> items) {
        boolean hasPizza = items.stream()
                .anyMatch(i -> i.getProduct().getName().toLowerCase().contains("pizza"));

        if (!hasPizza) return 0.0;

        return items.stream()
                .filter(i -> i.getProduct().getCategory().equalsIgnoreCase("DESERT"))
                .map(OrderItem::getPriceAtOrder)
                .min(Double::compare)
                .map(price -> price * 0.75)
                .orElse(0.0);
    }

    // Party Pack: Buy 4 Pizzas, cheapest is free
    private double applyPartyPack(List<OrderItem> items) {
        List<Double> pizzaPrices = new ArrayList<>();
        for (OrderItem item : items) {
            if (item.getProduct().getName().toLowerCase().contains("pizza")) {
                for (int i = 0; i < item.getQuantity(); i++) {
                    pizzaPrices.add(item.getPriceAtOrder());
                }
            }
        }

        if (pizzaPrices.size() < 4) return 0.0;

        pizzaPrices.sort(Comparator.naturalOrder()); // Cheapest first

        int freePizzas = pizzaPrices.size() / 4;
        double discount = 0;

        for (int i = 0; i < freePizzas; i++) {
            discount += pizzaPrices.get(i);
        }
        return discount;
    }
}
