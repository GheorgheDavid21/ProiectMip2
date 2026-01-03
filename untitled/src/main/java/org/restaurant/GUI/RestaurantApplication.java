package org.restaurant.GUI;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.restaurant.controller.HistoryController;
import org.restaurant.controller.MenuController;
import org.restaurant.controller.OffersController;
import org.restaurant.persistence.OrderRepository;
import org.restaurant.persistence.PersistenceManager;
import org.restaurant.persistence.ProductRepository;
import org.restaurant.view.ProductProfile;
import org.restaurant.model.Drink;
import org.restaurant.model.Food;
import org.restaurant.model.Product;

import java.io.FileReader;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;



public class RestaurantApplication extends Application {
    @Override
    public void start(Stage stage) {
    ProductRepository productRepository = new ProductRepository();
    OrderRepository orderRepository = new OrderRepository();

    MenuView menuView = new MenuView();

    new MenuController(menuView, productRepository);

    HistoryView historyView = new HistoryView();
    HistoryController historyController = new HistoryController(historyView, orderRepository);

    OffersView offersView = new OffersView();
    new OffersController(offersView);

    TabPane tabPane = new TabPane();

    Tab menuTab = new Tab("Meniu", menuView.getView());
    menuTab.setClosable(false);

    Tab historyTab = new Tab("Istoric comenzi", historyView.getView());
    historyTab.setClosable(false);

    historyTab.setOnSelectionChanged(event -> {
        if (historyTab.isSelected()) {
            historyController.refreshData();
        }
    });

    Tab offersTab = new Tab("Oferte", offersView.getView());
    offersTab.setClosable(false);

    tabPane.getTabs().addAll(menuTab, historyTab, offersTab);

    Scene scene = new Scene(tabPane, 1000, 600);
    stage.setTitle("Restaurant \"La Andrei\"");
    stage.setScene(scene);
    stage.show();

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
//    private void loadProducts(ObservableList<ProductProfile> dest) {
//        Gson gson = new Gson();
//        try (FileReader reader = new FileReader(EXPORT_FILE)) {
//            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
//            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
//                String catName = entry.getKey();
//                MenuProducts.Category category;
//                try {
//                    category = MenuProducts.Category.valueOf(catName);
//                } catch (IllegalArgumentException ex) {
//                    continue;
//                }
//                for (JsonElement el : entry.getValue().getAsJsonArray()) {
//                    JsonObject obj = el.getAsJsonObject();
//                    try {
//                        String name = obj.has("name") ? obj.get("name").getAsString() : "unnamed";
//                        double price = obj.has("price") ? obj.get("price").getAsDouble() : 0.0;
//                        Product p;
//                        if (obj.has("weight")) {
//                            int weight = obj.get("weight").getAsInt();
//                            p = new Food(name, price, weight);
//                        } else if (obj.has("volume")) {
//                            int volume = obj.get("volume").getAsInt();
//                            p = new Drink(name, price, volume);
//                        } else {
//                            continue;
//                        }
//                        dest.add(new ProductProfile(p, category));
//                    } catch (Exception ignore) { }
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Could not load " + EXPORT_FILE + ": " + e.getMessage());
//        }
//    }

    @Override
    public void stop() {
        PersistenceManager.getInstance().close();
    }

}

