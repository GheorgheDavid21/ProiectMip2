package org.restaurant.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.restaurant.controller.*;
import org.restaurant.model.User;
import org.restaurant.persistence.OrderRepository;
import org.restaurant.persistence.PersistenceManager;
import org.restaurant.persistence.ProductRepository;
import org.restaurant.persistence.UserRepository;


public class RestaurantApplication extends Application {
    private final ProductRepository productRepository = new ProductRepository();
    private final OrderRepository orderRepository = new OrderRepository();
    private final UserRepository userRepository = new UserRepository();

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Restaurant \"La Andrei\"");

        showGuestView();
        primaryStage.show();
    }
    public void showGuestView() {
        GuestView guestView = new GuestView();
        new GuestController(guestView, productRepository, this);
        Scene scene = new Scene(guestView.getView(), 1000, 600);
        primaryStage.setScene(scene);
    }

    public void showLoginView() {
        LoginView loginView = new LoginView(this);
        new LoginController(loginView, new org.restaurant.persistence.UserRepository(), this);
        Scene scene = new Scene(loginView.getView(), 400, 300);
        primaryStage.setScene(scene);
    }

    public void showWaiterDashboard(User loggedUser) {
        HistoryView historyView = new HistoryView();

        WaiterDashboard dashboard = new WaiterDashboard(this, historyView, new HistoryController(historyView, orderRepository, loggedUser), loggedUser);
        new WaiterController(dashboard, productRepository, this, orderRepository);
        Scene scene = new Scene(dashboard.getView(), 1100, 700);
        primaryStage.setScene(scene);
    }
    public void showManagerDashboard() {
        StaffView staffView = new StaffView();
        GuestView menuView = new GuestView(); // Reuse GuestView structure
        OffersView offersView = new OffersView();
        HistoryView historyView = new HistoryView();
        ProductDialog productDialog = new ProductDialog();

        ManagerDashboard dashboard = new ManagerDashboard(this, staffView, menuView, offersView, historyView);

        new ManagerController(this, dashboard, userRepository, orderRepository, staffView, menuView, offersView, historyView, productRepository, productDialog);

        Scene scene = new Scene(dashboard.getView(), 1100, 700);
        primaryStage.setScene(scene);
    }

    @Override
    public void stop() {
        PersistenceManager.getInstance().close();
    }

    }




