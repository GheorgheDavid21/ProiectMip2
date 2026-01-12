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

        ManagerDashboard dashboard = new ManagerDashboard(this, staffView, menuView, offersView, historyView);

        new ManagerController(this, dashboard, userRepository, orderRepository, staffView, menuView, offersView, historyView, productRepository);

        Scene scene = new Scene(dashboard.getView(), 1100, 700);
        primaryStage.setScene(scene);
    }


    @Override
    public void stop() {
        PersistenceManager.getInstance().close();
    }

//        new MenuController(menuView, productRepository);
//
//        HistoryView historyView = new HistoryView();
//        HistoryController historyController = new HistoryController(historyView, orderRepository);
//
//        OffersView offersView = new OffersView();
//        new OffersController(offersView);
//
//        TabPane tabPane = new TabPane();
//
//        Tab menuTab = new Tab("Meniu", menuView.getView());
//        menuTab.setClosable(false);
//
//        Tab historyTab = new Tab("Istoric comenzi", historyView.getView());
//        historyTab.setClosable(false);
//
//        historyTab.setOnSelectionChanged(event -> {
//            if (historyTab.isSelected()) {
//                historyController.refreshData();
//            }
//        });
//
//        Tab offersTab = new Tab("Oferte", offersView.getView());
//        offersTab.setClosable(false);
//
//        tabPane.getTabs().addAll(menuTab, historyTab, offersTab);
//
//        Scene scene = new Scene(tabPane, 1000, 600);
//        stage.setTitle("Restaurant \"La Andrei\"");
//        stage.setScene(scene);
//        stage.show();




//        Label titleLabel = new Label("Bun venit la Restaurantul \"La Andrei\"!");
//        titleLabel.setFont(new Font("System Bold", 20.0));
//
//        ObservableList<ProductProfile> products = FXCollections.observableArrayList();
//        loadProducts(products);
//
//        ListView<ProductProfile> productListView = new ListView<>(products);
//        productListView.setCellFactory(lv-> new ListCell<>(){
//            @Override
//            protected void updateItem(ProductProfile item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty || item == null ? null : item.getName().get());
//            }
//        });
//
//        GridPane form = new GridPane();
//        form.setPadding(new Insets(10));
//        form.setHgap(10);
//        form.setVgap(8);
//
//        Label nameLabel = new Label("Nume:");
//        Label nameValue = new Label();
//        Label categoryLabel = new Label("Categorie:");
//        Label categoryValue = new Label();
//        Label setPriceLabel = new Label("Setează preț (RON):");
//        Label priceLabel = new Label("Pret (RON):");
//        TextField priceField = new TextField();
//        Label priceValue = new Label();
//        Label measureLabel = new Label();
//        Label measureValue = new Label();
//
//
//
//        final Object[] currentBound = new Object[1];
//        AtomicBoolean selected = new AtomicBoolean(false);
//
//        productListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVm, newVm) -> {
//            if(!selected.get()) {
//                form.addRow(0, nameLabel, nameValue);
//                form.addRow(1, categoryLabel, categoryValue);
//                form.addRow(2, setPriceLabel, priceField);
//                form.addRow(3, priceLabel, priceValue);
//                form.addRow(4, measureLabel, measureValue);
//            }
//            selected.set(true);
//            if (currentBound[0] != null) {
//                priceField.textProperty().unbindBidirectional(currentBound[0]);
//                priceValue.textProperty().unbindBidirectional(currentBound[0]);
//            }
//
//            if (newVm == null) {
//                nameValue.setText("");
//                categoryValue.setText("");
//                priceField.setText("");
//                priceLabel.setText("");
//                measureValue.setText("");
//                currentBound[0] = null;
//            } else {
//                nameValue.setText(newVm.getName().get());
//                categoryValue.setText(newVm.getCategoryName().get());
//                NumberStringConverter converter = new NumberStringConverter();
//                priceField.textProperty().bindBidirectional(newVm.getPrice(), converter);
//                priceValue.textProperty().bind(Bindings.createStringBinding(
//                        () -> String.format("%.2f RON", newVm.getPrice().get()),
//                        newVm.getPrice()
//                ));
//                currentBound[0] = newVm.getPrice();
//
//                measureLabel.setText(newVm.getIsFood() ? ("Gramaj: ") : ("Volum: "));
//                measureValue.setText(newVm.getIsFood() ? (newVm.getMeasure().get() + " g")
//                        : (newVm.getMeasure().get() + " ml"));
//            }
//        });
//
//        BorderPane root = new BorderPane();
//        root.setTop(titleLabel);
//        root.setLeft(productListView);
//        root.setCenter(form);
//        BorderPane.setMargin(productListView, new Insets(10));
//        BorderPane.setMargin(form, new Insets(10));
//        BorderPane.setAlignment(titleLabel, Pos.CENTER);
//
//        MenuBar menuBar = new MenuBar();
//        Menu fileMenu = new Menu("File");
//        MenuItem importItem = new MenuItem("Import JSON");
//        MenuItem exportItem = new MenuItem("Export JSON");
//        fileMenu.getItems().addAll(importItem, exportItem);
//        menuBar.getMenus().add(fileMenu);
//        root.setTop(menuBar);
//
//        Scene scene = new Scene(root, 1000, 600);
//        stage.setTitle("Restaurant \"La Andrei\"");
//        stage.setScene(scene);
//        stage.show();
    }




