package org.restaurant.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import org.restaurant.GUI.HistoryView;
import org.restaurant.model.Order;
import org.restaurant.model.User;
import org.restaurant.model.UserRole;
import org.restaurant.persistence.OrderRepository;
import javafx.scene.control.TableColumn;

public class HistoryController {
    private final HistoryView view;
    private final OrderRepository orderRepository;
    private final User loggedUser;

    public HistoryController(HistoryView view, OrderRepository orderRepository, User loggedUser) {
        this.view = view;
        this.orderRepository = orderRepository;
        this.loggedUser = loggedUser;

        initTable();
        refreshData();
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
        if (loggedUser == null || loggedUser.getRole() == UserRole.MANAGER) {
            view.getOrderTable().setItems(FXCollections.observableArrayList(orderRepository.findAll()));
        } else {
            view.getOrderTable().setItems(FXCollections.observableArrayList(orderRepository.findOrdersByUser(loggedUser.getId())));
        }
    }
}
