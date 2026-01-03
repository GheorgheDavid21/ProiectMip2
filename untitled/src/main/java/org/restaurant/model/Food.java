package org.restaurant.model;


import jakarta.persistence.*;

@Entity
@DiscriminatorValue("FOOD")
public class Food extends Product {

    @Column(name = "weight")
    private int weight;

    protected Food() {
    }

    public Food(String name, double price, String category, int weight) {

        super(name, price, category);
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
    @Override
    public String toString() {
        return "> " + this.getName() + " - " + this.getPrice() + " RON - Gramaj: " + weight + "g";
    }


}

