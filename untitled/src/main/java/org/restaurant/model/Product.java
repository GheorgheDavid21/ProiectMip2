package org.restaurant.model;

public sealed interface Product permits org.restaurant.model.Food, org.restaurant.model.Drink {

    String getName();
    double getPrice();

}

