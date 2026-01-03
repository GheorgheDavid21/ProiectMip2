package org.restaurant;


import org.restaurant.lambda.Discount;
import org.restaurant.model.Drink;
import org.restaurant.model.Pizza;
import org.restaurant.model.Product;

import java.util.HashMap;


public class Order {
    //    static final double TVA = 0.09;
    static private double TVA;
    HashMap<Product, Integer> products;
    HashMap<Pizza, Integer> pizzas;
    public Order() {
        products = new HashMap<>();
        pizzas = new HashMap<>();
    }

    public void setTVA(double tva) {
        TVA = tva;
    }

    public void addProduct(Product product, int quantity) {
        products.put(product, products.getOrDefault(product, 0) + quantity);
    }
    public void addPizza(Pizza pizza, int quantity) {
        pizzas.put(pizza, products.getOrDefault(pizza, 0) + quantity);
    }

    public double getTotal() {
        double total = 0.0;
        for (Product product : products.keySet()) {
            int quantity = products.get(product);
            total += product.getPrice() * quantity;
            System.out.println(quantity + "x " + product.getName());
        }
        for (Pizza pizza : pizzas.keySet()) {
            int quantity = pizzas.get(pizza);
            total += pizza.getPrice() * quantity;
            System.out.println(quantity + "x " + pizza.getName());
        }
        return total + total * this.TVA;
    }
    public double getTotalWithDiscount(Discount discount, double discountRate) {
        if(discount == null || discountRate <= 0){
            return getTotal();
        }
        return discount.applyDiscount(this, discountRate);
    }

    Discount HappyHourDiscount = (order, discountRate) -> {
        double total = 0.0;
        for (Product product : products.keySet()) {
            int quantity = products.get(product);
            if(product instanceof Drink) {
                total += (product.getPrice() - product.getPrice() * discountRate) * quantity;
            }
            else{
                total += product.getPrice() * quantity;
            }
            System.out.println(quantity + "x " + product.getName());
        }
        for (Pizza pizza : pizzas.keySet()) {
            int quantity = pizzas.get(pizza);
            total += pizza.getPrice() * quantity;
            System.out.println(quantity + "x " + pizza.getName());
        }
        return total + total * this.TVA;
    };



}

