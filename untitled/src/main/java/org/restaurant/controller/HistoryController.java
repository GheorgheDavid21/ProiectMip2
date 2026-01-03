package org.restaurant.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import org.restaurant.GUI.HistoryView;
import org.restaurant.model.Order;
import org.restaurant.persistence.OrderRepository;
import javafx.scene.control.TableColumn;

public class HistoryController {
    private final HistoryView view;
    private final OrderRepository orderRepository;

    public HistoryController(HistoryView view, OrderRepository orderRepository) {
        this.view = view;
        this.orderRepository = orderRepository;

        initTable();
    }

    private void initTable() {
        TableColumn<Order, String> waiterCol = (TableColumn<Order, String>) view.getOrderTable().getColumns().get(2);
        waiterCol.setCellValueFactory(cellData -> {
            if (cellData.getValue().getUser() != null) {
                return new SimpleStringProperty(cellData.getValue().getUser().getUsername());
            }
            return new SimpleStringProperty("-");
        });
    }

    public void refreshData() {
        view.getOrderTable().setItems(FXCollections.observableArrayList(orderRepository.findAll()));
    }
}
