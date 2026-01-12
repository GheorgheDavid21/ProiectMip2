package org.restaurant.controller;

import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.restaurant.GUI.StaffView;
import org.restaurant.model.User;
import org.restaurant.model.UserRole;
import org.restaurant.persistence.UserRepository;

public class StaffController {
    private final StaffView view;
    private final UserRepository userRepository;

    public StaffController(StaffView view, UserRepository userRepository) {
        this.view = view;
        this.userRepository = userRepository;
        initEvents();
        refresh();
    }

    private void initEvents() {
        view.getAddBtn().setOnAction(e -> handleAdd());
        view.getDeleteBtn().setOnAction(e -> handleDelete());
    }

    private void handleAdd() {
        String u = view.getUsernameField().getText();
        String p = view.getPasswordField().getText();
        if(u.isEmpty() || p.isEmpty()) return;

        User newUser = new User(u, p, UserRole.WAITER);
        try {
            userRepository.saveOrUpdate(newUser);
            view.getUsernameField().clear();
            view.getPasswordField().clear();
            refresh();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Eroare la adaugare: " + ex.getMessage()).show();
        }
    }

    private void handleDelete() {
        User selected = view.getUserTable().getSelectionModel().getSelectedItem();
        if(selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "STERGERE CRITICA: Stergerea ospatarului " + selected.getUsername() + " va sterge si toate comenzile lui din baza de date! Continuati?",
                ButtonType.YES, ButtonType.NO);

        if(alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            userRepository.delete(selected);
            refresh();
        }
    }

    private void refresh() {
        view.getUserTable().setItems(FXCollections.observableArrayList(
                userRepository.findAll().stream()
                        .filter(u -> u.getRole() == UserRole.WAITER)
                        .toList()
        ));
    }
}
