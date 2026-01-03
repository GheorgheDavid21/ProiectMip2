package org.restaurant.model;

public final class Drink implements Product {
    private String name;
    private double price;
    private int volume;
    public Drink(String name, double price, int volume) {
        this.name = name;
        this.price = price;
        this.volume = volume;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getVolume() {
        return volume;
    }
    @Override
    public String toString() {
        return "> " + name + " - " + price + " RON - Volum: " + volume + "ml";
    }

}

