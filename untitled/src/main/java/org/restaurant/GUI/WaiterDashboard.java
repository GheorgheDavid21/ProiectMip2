package org.restaurant.GUI;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.restaurant.controller.HistoryController;
import org.restaurant.model.User;
import org.restaurant.persistence.OrderRepository;
import org.restaurant.view.OrderItemView;
import org.restaurant.view.ProductProfile;

public class WaiterDashboard {
    private final RestaurantApplication app;
    private final HistoryView historyView;
    private final HistoryController historyController; // Add field
    private final User loggedUser;
    private final BorderPane root;

    private ListView<ProductProfile> menuTable;
    private ListView<OrderItemView> cartList;
    private Spinner<Integer> tableSpinner;
    private Label totalLabel;

    private Button addBtn;
    private Button removeBtn;
    private Button placeOrderBtn;

    private TextField detName;
    private TextField detCategory;
    private TextField detPrice;
    private TextField detMeasure;


    private Tab histTab;

    public WaiterDashboard(RestaurantApplication app, HistoryView historyView, HistoryController historyController, User user) {
        this.app = app;
        this.historyView = historyView;
        this.historyController = historyController;
        this.loggedUser = user;

        root = new BorderPane();

        BorderPane header = new BorderPane();
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #add8e6; -fx-border-width: 0 0 1 0; -fx-border-color: #999;");
        header.setLeft(new Label("Ospatar: " + user.getUsername()));
        Button logout = new Button("Deconectare");
        logout.setOnAction(e -> app.showGuestView());
        header.setRight(logout);
        root.setTop(header);

        TabPane tabs = new TabPane();

        Tab orderTab = new Tab("Comanda Noua");
        orderTab.setClosable(false);
        orderTab.setContent(createOrderView());

        histTab = new Tab("Istoricul Meu");
        histTab.setClosable(false);
        histTab.setContent(historyView.getView());

        tabs.getTabs().addAll(orderTab, histTab);
        root.setCenter(tabs);
    }

    private Parent createOrderView() {
        BorderPane pane = new BorderPane();

        menuTable = new ListView<>();
        menuTable.setCellFactory(param -> new ListCell<>() {
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

        pane.setCenter(menuTable);

        SplitPane sidebar = new SplitPane();
        sidebar.setOrientation(Orientation.VERTICAL);
        sidebar.setPrefWidth(320);
        sidebar.setDividerPositions(0.35);

        VBox detailsBox = new VBox(10);
        detailsBox.setPadding(new Insets(15));
        detailsBox.setStyle("-fx-background-color: #f9f9f9;");

        Label detLabel = new Label("Detalii Produs");
        detLabel.setStyle("-fx-font-weight: bold; -fx-underline: true;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);

        detName = new TextField(); detName.setEditable(false);
        detCategory = new TextField(); detCategory.setEditable(false);
        detPrice = new TextField(); detPrice.setEditable(false);
        detMeasure = new TextField(); detMeasure.setEditable(false);

        grid.addRow(0, new Label("Nume:"), detName);
        grid.addRow(1, new Label("Categorie:"), detCategory);
        grid.addRow(2, new Label("Preț:"), detPrice);
        grid.addRow(3, new Label("Cantitate:"), detMeasure);

        addBtn = new Button("Adauga în Cos");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setStyle("-fx-base: #b6e7c9; -fx-font-weight: bold;");

        detailsBox.getChildren().addAll(detLabel, grid, new Region(), addBtn);

        VBox cartBox = new VBox(10);
        cartBox.setPadding(new Insets(15));

        HBox tableBox = new HBox(10);
        tableBox.setAlignment(Pos.CENTER_LEFT);
        tableBox.getChildren().add(new Label("Masa Nr:"));
        tableSpinner = new Spinner<>(1, 50, 1);
        tableBox.getChildren().add(tableSpinner);

        cartList = new ListView<>();
        cartList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(OrderItemView item, boolean empty) {
                super.updateItem(item, empty);

                textProperty().unbind();

                if (empty || item == null) {
                    setText(null);
                } else {
                    textProperty().bind(Bindings.createStringBinding(
                            () -> item.getProductName() + " x" + item.getQuantity() + " - " + item.getPriceAtOrder() + " RON",
                            item.quantityProperty()
                    ));
                }
            }
        });
        VBox.setVgrow(cartList, Priority.ALWAYS);

        HBox cartControls = new HBox(10);
        removeBtn = new Button("Scoate din cos");


        cartControls.getChildren().addAll(removeBtn);


        totalLabel = new Label("Total: 0.0 RON");
        totalLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        placeOrderBtn = new Button("PLASEAZA COMANDA");
        placeOrderBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        placeOrderBtn.setMaxWidth(Double.MAX_VALUE);
        placeOrderBtn.setPrefHeight(40);

        cartBox.getChildren().addAll(tableBox, new Label("Cos Curent:"), cartList, cartControls, new Separator(), totalLabel, placeOrderBtn);

        placeOrderBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        placeOrderBtn.setMaxWidth(Double.MAX_VALUE);

        sidebar.getItems().addAll(detailsBox, cartBox);
        pane.setRight(sidebar);

        return pane;
    }

    public BorderPane getRoot() {
        return root;
    }

    public ListView<ProductProfile> getMenuTable() {
        return menuTable;
    }

    public ListView<OrderItemView> getCartList() {
        return cartList;
    }

    public Spinner<Integer> getTableSpinner() {
        return tableSpinner;
    }

    public Label getTotalLabel() {
        return totalLabel;
    }

    public Parent getView() { return root; }

    public Button getAddBtn() { return addBtn;}

    public Button getRemoveBtn() { return removeBtn;}

    public TextField getDetName() {
        return detName;
    }

    public TextField getDetCategory() {
        return detCategory;
    }

    public TextField getDetPrice() {
        return detPrice;
    }

    public TextField getDetMeasure() {
        return detMeasure;
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public Button getPlaceOrderBtn() {
        return placeOrderBtn;
    }
    public Tab getHistTab() {
        return histTab;
    }
    public HistoryController getHistoryController() {
        return historyController;
    }
}
