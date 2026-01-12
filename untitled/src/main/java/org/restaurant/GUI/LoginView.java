package org.restaurant.GUI;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.restaurant.model.User;
import org.restaurant.model.UserRole;

public class LoginView {
    private final RestaurantApplication app;
    private final VBox root;
    private final TextField userField;
    private final PasswordField passField;
    private final Button loginBtn;

    public LoginView(RestaurantApplication app) {
        this.app = app;
        root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 30;");

        Label title = new Label("Autentificare Staff");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        userField = new TextField();
        userField.setPromptText("Utilizator");

        passField = new PasswordField();
        passField.setPromptText("Parola");

        loginBtn = new Button("Login");

        Button cancelBtn = new Button("Inapoi la Meniu");
        cancelBtn.setOnAction(e -> app.showGuestView());

        root.getChildren().addAll(title, userField, passField, loginBtn, cancelBtn);
    }


    public TextField getUserField() {
        return userField;
    }

    public PasswordField getPassField() {
        return passField;
    }

    public Button getLoginBtn() {
        return loginBtn;
    }

    public Parent getView() { return root; }
}
