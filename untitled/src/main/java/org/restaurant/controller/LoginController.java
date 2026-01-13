package org.restaurant.controller;

import org.restaurant.GUI.LoginView;
import org.restaurant.GUI.RestaurantApplication;
import org.restaurant.model.User;
import org.restaurant.persistence.UserRepository;

public class LoginController {
    private final LoginView view;
    private final UserRepository userRepository;
    private final RestaurantApplication app;


    public LoginController(LoginView view, UserRepository userRepository, RestaurantApplication app) {
        this.view = view;
        this.userRepository = userRepository;
        this.app = app;

        initEvents();
    }

    private void handleLogin() {
        String username = view.getUserField().getText();
        String password = view.getPassField().getText();

        if (username.isEmpty() || password.isEmpty()) {
            return;
        }
        User user = userRepository.validateCredentials(username, password);
        if (user != null) {
            switch (user.getRole()) {
                case MANAGER -> app.showManagerDashboard();
                case WAITER -> app.showWaiterDashboard(user);
                default -> {
                }
            }
        }
    }

    private void initEvents() {
        view.getLoginBtn().setOnAction(e -> handleLogin());
    }
}
