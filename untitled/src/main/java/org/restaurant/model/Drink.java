package org.restaurant.model;

import jakarta.persistence.*;


@Entity
@DiscriminatorValue("DRINK")
public class Drink extends Product {

    @Column(name = "volume")
    private int volume;

    protected Drink() {
    }

    public Drink(String name, double price, String category, int volume) {
        super(name, price, category);
        this.volume = volume;
    }

    public int getVolume() {
        return volume;
    }
    @Override
    public String toString() {
        return "> " + this.getName() + " - " + this.getPrice() + " RON - Volum: " + volume + "ml";
    }

}

