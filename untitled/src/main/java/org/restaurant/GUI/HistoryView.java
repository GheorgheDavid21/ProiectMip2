package org.restaurant.GUI;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.restaurant.model.Order;

public class HistoryView {
    private final VBox root;
    private final TableView<Order> orderTable;

    public HistoryView() {
        root = new VBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        Label title = new Label("Istoric Comenzi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        orderTable = new TableView<>();

        TableColumn<Order, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order, String> dateCol = new TableColumn<>("Data");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        TableColumn<Order, Integer> tableCol = new TableColumn<>("Masa");
        tableCol.setCellValueFactory(new PropertyValueFactory<>("tableNumber"));

        TableColumn<Order, Double> totalCol = new TableColumn<>("Total (RON)");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        TableColumn<Order, String> waiterCol = new TableColumn<>("Ospatar");
        // Cell value factory for waiter will be set in controller or here if simple

        orderTable.getColumns().addAll(idCol, dateCol, waiterCol, tableCol, totalCol);
        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        root.getChildren().addAll(title, orderTable);
    }

    public Parent getView() { return root; }
    public TableView<Order> getOrderTable() { return orderTable; }
}

