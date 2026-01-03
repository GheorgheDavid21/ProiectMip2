package org.restaurant.controller;

import org.restaurant.GUI.OffersView;

public class OffersController {
    private final OffersView view;

    // Static state to be accessible globally for calculation logic later
    public static boolean HAPPY_HOUR_ACTIVE = false;
    public static boolean MEAL_DEAL_ACTIVE = false;
    public static boolean PARTY_PACK_ACTIVE = false;

    public OffersController(OffersView view) {
        this.view = view;
        initEvents();
    }

    private void initEvents() {
        view.getHappyHourCheck().selectedProperty().addListener((obs, oldV, newV) -> HAPPY_HOUR_ACTIVE = newV);
        view.getMealDealCheck().selectedProperty().addListener((obs, oldV, newV) -> MEAL_DEAL_ACTIVE = newV);
        view.getPartyPackCheck().selectedProperty().addListener((obs, oldV, newV) -> PARTY_PACK_ACTIVE = newV);
    }
}
