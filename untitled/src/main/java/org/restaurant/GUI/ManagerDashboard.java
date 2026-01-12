package org.restaurant.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ManagerDashboard {
    private final RestaurantApplication app;
    private final BorderPane root;
    private final TabPane tabPane;

    private final Button addProductBtn;
    private final Button delProductBtn;
//    private final Button logoutBtn;

    private final MenuItem importItem;
    private final MenuItem exportItem;

    public ManagerDashboard(RestaurantApplication app,
                            StaffView staffView,
                            GuestView menuView,
                            OffersView offersView,
                            HistoryView historyView) {
        this.app = app;
        root = new BorderPane();


        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        importItem = new MenuItem("Import JSON");
        exportItem = new MenuItem("Export JSON");

        fileMenu.getItems().addAll(importItem, exportItem);
        menuBar.getMenus().addAll(fileMenu);

        HBox header = new HBox(15);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Manager Dashboard");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        menuView.getLoginButton().setText("Deconectare");
//        logoutBtn = new Button("Deconectare");
//        logoutBtn.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
//        logoutBtn.setOnAction(e -> app.showGuestView());



        header.getChildren().addAll(title, spacer );
        VBox topContainer = new VBox(menuBar, header);
        root.setTop(topContainer);

        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        BorderPane menuWrapper = new BorderPane();
        menuWrapper.setCenter(menuView.getView());

        HBox menuControls = new HBox(10);
        menuControls.setPadding(new Insets(10));
        menuControls.setAlignment(Pos.CENTER_RIGHT);
        menuControls.setStyle("-fx-background-color: #eee; -fx-border-color: #ccc; -fx-border-width: 1 0 0 0;");

        addProductBtn = new Button("Adauga Produs Nou");
        addProductBtn.setStyle("-fx-base: #5cb85c; -fx-font-weight: bold;"); // Green style

        delProductBtn = new Button("Sterge Produs Selectat");
        delProductBtn.setStyle("-fx-base: #d9534f;"); // Red style

        menuControls.getChildren().addAll(addProductBtn, delProductBtn);
        menuWrapper.setBottom(menuControls);

        Tab menuTab = new Tab("Gestiune Meniu", menuWrapper);
        Tab staffTab = new Tab("Gestiune Personal", staffView.getView());
        Tab historyTab = new Tab("Istoric Comenzi", historyView.getView());
        Tab offersTab = new Tab("Oferte Speciale", offersView.getView());




        fileMenu.getItems().addAll(importItem, exportItem);


        root.setTop(menuBar);

        tabPane.getTabs().addAll(menuTab, staffTab, historyTab, offersTab);
        root.setCenter(tabPane);
    }

    public Parent getView() {
        return root;
    }

    public Button getAddProductBtn() {
        return addProductBtn;
    }

    public Button getDelProductBtn() {
        return delProductBtn;
    }

//    public Button getLogoutBtn() {
//        return logoutBtn;
//    }

    public MenuItem getMenuExportBtn() {
        return exportItem;
    }
    public MenuItem getMenuImportBtn() {
        return importItem;
    }
}
