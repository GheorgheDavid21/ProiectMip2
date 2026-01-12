package org.restaurant.GUI;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.restaurant.model.User;

public class StaffView {
    private final VBox root;
    private final TableView<User> userTable;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button addBtn;
    private final Button deleteBtn;

    public StaffView() {
        root = new VBox(10);
        root.setPadding(new Insets(10));

        Label title = new Label("Gestiune Personal (Ospatari)");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        userTable = new TableView<>();

        TableColumn<User, Long> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> userCol = new TableColumn<>("Username");
        userCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        userTable.getColumns().addAll(idCol, userCol, roleCol);
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(userTable, Priority.ALWAYS);

        HBox controls = new HBox(10);
        usernameField = new TextField(); usernameField.setPromptText("Username");
        passwordField = new PasswordField(); passwordField.setPromptText("Password");
        addBtn = new Button("Adauga Ospatar");
        deleteBtn = new Button("Sterge Selectat");
        deleteBtn.setStyle("-fx-background-color: #ffcccc; -fx-text-fill: red;");

        controls.getChildren().addAll(usernameField, passwordField, addBtn, deleteBtn);

        root.getChildren().addAll(title, userTable, controls);
    }

    public Parent getView() { return root; }
    public TableView<User> getUserTable() { return userTable; }
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getPasswordField() { return passwordField; }
    public Button getAddBtn() { return addBtn; }
    public Button getDeleteBtn() { return deleteBtn; }
}
