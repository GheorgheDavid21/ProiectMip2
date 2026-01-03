package org.restaurant.GUI;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OffersView {
    private final VBox root;
    private final CheckBox happyHourCheck;
    private final CheckBox mealDealCheck;
    private final CheckBox partyPackCheck;

    public OffersView() {
        root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(15);

        Label title = new Label("Gestiune Oferte Active");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        happyHourCheck = new CheckBox("Happy Hour Drinks: Fiecare a doua băutură are reducere 50%");
        mealDealCheck = new CheckBox("Meal Deal: La orice Pizza, cel mai ieftin desert are reducere 25%");
        partyPackCheck = new CheckBox("Party Pack: La 4 Pizza comandate, una (cea mai ieftină) este gratuită");

        root.getChildren().addAll(title, happyHourCheck, mealDealCheck, partyPackCheck);
    }

    public Parent getView() { return root; }
    public CheckBox getHappyHourCheck() { return happyHourCheck; }
    public CheckBox getMealDealCheck() { return mealDealCheck; }
    public CheckBox getPartyPackCheck() { return partyPackCheck; }
}
