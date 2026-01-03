package org.restaurant.model;

public final class Food implements Product {

    private String name;
    private double price;
    private int weight;
    public Food(String name, double price, int weight) {
        this.name = name;
        this.price = price;
        this.weight = weight;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public double getPrice() {
        return price;
    }
    public int getWeight() {
        return weight;
    }
    @Override
    public String toString() {
        return "> " + name + " - " + price + " RON - Gramaj: " + weight + "g";
    }

    public void setPrice(double price) {
        this.price = price;
    }

}

