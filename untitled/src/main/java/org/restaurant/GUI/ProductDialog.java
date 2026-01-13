package org.restaurant.GUI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.restaurant.model.Drink;
import org.restaurant.model.Food;
import org.restaurant.model.Product;

public class ProductDialog extends Dialog<Product> {

    private final ButtonType saveButtonType;
    private final TextField nameField;
    private final TextField priceField;
    private final TextField categoryField;
    private final TextField measureField;
    private final ComboBox<String> typeCombo;

    public ProductDialog() {
        this.setTitle("Adauga Produs Nou");
        this.setHeaderText("Introdu specificatiile produsului:");

        saveButtonType = new ButtonType("Salveaza", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        nameField = new TextField();
        nameField.setPromptText("Nume Produs");

        priceField = new TextField();
        priceField.setPromptText("Pret");

        categoryField = new TextField();
        categoryField.setPromptText("Categorie");

        measureField = new TextField();
        measureField.setPromptText("Gramaj (g)");

        typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Mancare", "Bautura");
        typeCombo.setValue("Mancare");

        typeCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            measureField.setPromptText(newVal.equals("Mancare") ? "Gramaj (g)" : "Volum (ml)");
        });

        grid.add(new Label("Tip:"), 0, 0);
        grid.add(typeCombo, 1, 0);
        grid.add(new Label("Nume:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Pret:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Categorie:"), 0, 3);
        grid.add(categoryField, 1, 3);
        grid.add(new Label("Cantitate:"), 0, 4);
        grid.add(measureField, 1, 4);

        this.getDialogPane().setContent(grid);

//        this.setResultConverter(dialogButton -> {
//            if (dialogButton == saveButtonType) {
//                try {
//                    String name = nameField.getText();
//                    double price = Double.parseDouble(priceField.getText());
//                    String category = categoryField.getText();
//                    int measure = Integer.parseInt(measureField.getText());
//
//                    if (typeCombo.getValue().equals("Mancare")) {
//                        return new Food(name, price, category, measure);
//                    } else {
//                        return new Drink(name, price, category, measure);
//                    }
//                } catch (Exception ex) {
//                    return null;
//                }
//            }
//            return null;
//        });
    }

    public ButtonType getSaveButtonType() {
        return saveButtonType;
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getPriceField() {
        return priceField;
    }

    public TextField getCategoryField() {
        return categoryField;
    }

    public TextField getMeasureField() {
        return measureField;
    }

    public ComboBox<String> getTypeCombo() {
        return typeCombo;
    }
}
