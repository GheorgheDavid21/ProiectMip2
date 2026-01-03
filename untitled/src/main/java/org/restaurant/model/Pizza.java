package org.restaurant.model;

import java.util.ArrayList;

public class Pizza {
    public enum Dough {
        CRISPY,
        FLUFFY
    }
    public enum Sauce {
        TOMATO,
        HOT,
        BARBECUE
    }
    private String name;
    private double price;
    private int weight;
    private Dough dough;
    private Sauce sauce;
    private ArrayList<String> ingredients;
    public Pizza(String name, double price, int weight, Dough dough, Sauce sauce) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.dough = dough;
        this.sauce = sauce;
    }
    private Pizza(Builder builder){
        this.name = builder.name;
        this.price = builder.price;
        this.weight = builder.weight;
        this.ingredients = builder.ingredients;
        this.dough = builder.dough;
        this.sauce = builder.sauce;
    }
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getWeight() {
        return weight;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public Dough getDough() {
        return dough;
    }
    public Sauce getSauce() {
        return sauce;
    }

    public static class Builder {
        private String name;
        private double price;
        private int weight;
        private Dough dough;
        private Sauce sauce;

        private ArrayList<String> ingredients  = new ArrayList<>();

        public Builder(String name,  double price,  int weight, Dough dough, Sauce sauce) {
            this.name = name;
            this.price = price;
            this.weight = weight;
            this.dough = dough;
            this.sauce = sauce;
        }

        public Pizza.Builder addIngredients(ArrayList<String> ingredients) {
            this.ingredients=ingredients;
            return this;
        }
        public Pizza build(){
            return new Pizza(this);
        }
    }
}

