package org.restaurant.GUI;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.restaurant.view.ProductProfile;

public class GuestView {
    private final BorderPane root;
    private final TextField searchField;
    private final Button loginButton;
    private final ListView<ProductProfile> productList;

    private final ComboBox<String> typeFilter;
    private final CheckBox vegFilter;
    private final TextField minPriceField;
    private final TextField maxPriceField;

    private final Button resetBtn;

    private final TextField nameField;
    private final TextField categoryField;
    private final TextField measureField;
    private final TextField priceField;

    private final Button saveDetailsBtn;






    public GuestView() {
        root = new BorderPane();

        HBox topContainer = new HBox(10);
        topContainer.setPadding(new Insets(10));
        topContainer.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-width: 0 0 1 0;");
        topContainer.setAlignment(Pos.CENTER_LEFT);

        searchField = new TextField();
        searchField.setPromptText("Caută produs...");
        searchField.setPrefWidth(300);

        typeFilter = new ComboBox<>();
        typeFilter.setItems(FXCollections.observableArrayList("Toate", "Mancare", "Bautura"));
        typeFilter.getSelectionModel().selectFirst();

        vegFilter = new CheckBox("Doar Vegetariene");

        minPriceField = new TextField();
        minPriceField.setPromptText("Min");
        minPriceField.setPrefWidth(60);

        maxPriceField = new TextField();
        maxPriceField.setPromptText("Max");
        maxPriceField.setPrefWidth(60);

        resetBtn = new Button("Resetează");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        loginButton = new Button("Autentificare Staff");

        topContainer.getChildren().addAll(new Label("Meniu:"), searchField,typeFilter,
                vegFilter, minPriceField, maxPriceField, resetBtn, spacer, loginButton);

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
//        importItem = new MenuItem("Import JSON");
//        exportItem = new MenuItem("Export JSON");
//        fileMenu.getItems().addAll(importItem, exportItem);

        VBox topSection = new VBox(menuBar, topContainer);
        root.setTop(topSection);

        productList = new ListView<>();
        productList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ProductProfile item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName().get());
                }
            }
        });

        root.setCenter(productList);
        BorderPane.setMargin(productList, new Insets(10));

        GridPane detailsForm = new GridPane();
        detailsForm.setPadding(new Insets(10));
        detailsForm.setHgap(10);
        detailsForm.setVgap(10);
        detailsForm.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #ddd; -fx-border-width: 0 0 0 1;");

        nameField = new TextField(); nameField.setEditable(false);
        categoryField = new TextField(); categoryField.setEditable(false);
        measureField = new TextField(); measureField.setEditable(false);
        priceField = new TextField(); priceField.setEditable(false); // Default read-only

        detailsForm.addRow(0, new Label("Nume:"), nameField);
        detailsForm.addRow(1, new Label("Categorie:"), categoryField);
        detailsForm.addRow(2, new Label("Gramaj/Vol:"), measureField);
        detailsForm.addRow(3, new Label("Preț:"), priceField);

        saveDetailsBtn = new Button("Salvează Modificări");
        saveDetailsBtn.setStyle("-fx-base: #b6e7c9; -fx-font-weight: bold;");
        saveDetailsBtn.setMaxWidth(Double.MAX_VALUE);
        saveDetailsBtn.setVisible(false);
        saveDetailsBtn.setManaged(false);

        saveDetailsBtn.visibleProperty().addListener((obs, oldV, newV) -> saveDetailsBtn.setManaged(newV));

        detailsForm.add(saveDetailsBtn, 1, 4);
        root.setRight(detailsForm);
    }

    public Parent getView() { return root; }

    public Button getLoginButton() { return loginButton; }
    public TextField getSearchField() { return searchField; }
    public ListView<ProductProfile> getProductList() { return productList; }
    public TextField getNameField() { return nameField; }
    public TextField getCategoryField() { return categoryField; }
    public TextField getMeasureField() { return measureField; }
    public TextField getPriceField() { return priceField; }
//    public MenuItem getImportMenuItem() { return importItem; }
//    public MenuItem getExportMenuItem() { return exportItem; }

    public ComboBox<String> getTypeFilter() {
        return typeFilter;
    }

    public CheckBox getVegFilter() {
        return vegFilter;
    }

    public TextField getMinPriceField() {
        return minPriceField;
    }

    public TextField getMaxPriceField() {
        return maxPriceField;
    }
    public Button getResetBtn() {
        return resetBtn;
    }
    public Button getSaveDetailsBtn() {
        return saveDetailsBtn;
    }

}