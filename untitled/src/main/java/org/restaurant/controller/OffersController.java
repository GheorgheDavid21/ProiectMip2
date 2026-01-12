package org.restaurant.controller;

import javafx.scene.control.Alert;
import org.restaurant.GUI.OffersView;

public class OffersController {
    private final OffersView view;

    public static boolean HAPPY_HOUR_ACTIVE = false;
    public static boolean MEAL_DEAL_ACTIVE = false;
    public static boolean PARTY_PACK_ACTIVE = false;

    public OffersController(OffersView view) {
        this.view = view;
        initEvents();
    }

    private void initEvents() {
        view.getSaveBtn().setOnAction(e -> {
            HAPPY_HOUR_ACTIVE = view.getHappyHourCheck().isSelected();
            MEAL_DEAL_ACTIVE = view.getMealDealCheck().isSelected();
            PARTY_PACK_ACTIVE = view.getPartyPackCheck().isSelected();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ofertele active au fost actualizate!");
            alert.show();
        });
    }
}
