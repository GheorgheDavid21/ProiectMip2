package org.restaurant.GUI;


import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import org.restaurant.view.ProductProfile;

public class MenuView {
//    private final NavigationManager navigationManager;
//    private final ProductRepository productRepository = new ProductRepository();
//    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final BorderPane root;
    private final ListView<ProductProfile> productList;
    private final TextField nameField;
    private final TextField priceField;
    private final TextField categoryField;
    private final TextField measureField;

    private final MenuItem importMenuItem;
    private final MenuItem exportMenuItem;

    public MenuView() {
        root = new BorderPane();
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        importMenuItem = new MenuItem("Import JSON");
        exportMenuItem = new MenuItem("Export JSON");
        fileMenu.getItems().addAll(importMenuItem, exportMenuItem);
        menuBar.getMenus().add(fileMenu);
        root.setTop(menuBar);


        productList = new ListView<>();

        productList.setCellFactory(listView -> new ListCell<>() {
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
        productList.setPrefWidth(250);

        VBox leftPane = new VBox(new Label("Meniu"), productList);
        leftPane.setSpacing(5);
        leftPane.setPadding(new Insets(10));

        GridPane detailsForm = new GridPane();
        detailsForm.setHgap(10);
        detailsForm.setVgap(10);
        detailsForm.setPadding(new Insets(20));

        nameField = new TextField();
        priceField = new TextField();
        categoryField = new TextField();
        measureField = new TextField();

        detailsForm.addRow(0, new Label("Nume produs:"), nameField);
        detailsForm.addRow(1, new Label("Categorie:"), categoryField);
        detailsForm.addRow(2, new Label("Gramaj/Volum:"), measureField);
        detailsForm.addRow(3, new Label("Preț (RON):"), priceField);

        VBox rightPane = new VBox(new Label("Detalii produs"), detailsForm);
        rightPane.setSpacing(10);
        rightPane.setPadding(new Insets(10));

        root.setLeft(leftPane);
        root.setCenter(rightPane);
    }

    public Parent getView() {
        return root;
    }

    public ListView<ProductProfile> getProductList() { return productList; }
    public TextField getNameField() { return nameField; }
    public TextField getPriceField() { return priceField; }
    public TextField getCategoryField() { return categoryField; }
    public TextField getMeasureField() { return measureField; }

    public MenuItem getImportMenuItem() { return importMenuItem; }
    public MenuItem getExportMenuItem() { return exportMenuItem; }
}
