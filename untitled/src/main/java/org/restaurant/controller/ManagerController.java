package org.restaurant.controller;

import com.google.gson.*;
import javafx.stage.FileChooser;
import org.restaurant.GUI.*;
import org.restaurant.model.Drink;
import org.restaurant.model.Food;
import org.restaurant.model.Product;
import org.restaurant.persistence.OrderRepository;
import org.restaurant.persistence.ProductRepository;
import org.restaurant.persistence.UserRepository;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;

public class ManagerController {
    private final RestaurantApplication app;
    private final ManagerDashboard dashboard;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final StaffController staffController;
    private final MenuController menuController;
    private final HistoryController historyController;
    private final OffersController offersController;

    private final Gson gson;

    public ManagerController(RestaurantApplication app, ManagerDashboard dashboard,
                             UserRepository userRepository,
                             OrderRepository orderRepository,
                             StaffView staffView,
                             GuestView menuView,
                             OffersView offersView,
                             HistoryView historyView,
                             ProductRepository productRepository, ProductDialog productDialog) {
        this.app = app;
        this.dashboard = dashboard;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.staffController = new StaffController(staffView, userRepository);
        this.menuController = new MenuController(menuView, productRepository, app, productDialog);
        this.menuController.enableManagerMode(); // Enable edit capability for manager
        this.historyController = new HistoryController(historyView, orderRepository, null);
        this.offersController = new OffersController(offersView);

        this.gson = new GsonBuilder().setPrettyPrinting().create();

        initEvents();
    }

    private void initEvents() {
        dashboard.getAddProductBtn().setOnAction(e -> menuController.addNewProduct());
        dashboard.getDelProductBtn().setOnAction(e -> menuController.deleteSelectedProduct());
        dashboard.getMenuExportBtn().setOnAction(e -> handleExport());
        dashboard.getMenuImportBtn().setOnAction(e -> handleImport());
    }

    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Meniu JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(dashboard.getView().getScene().getWindow());

        if (file != null) {
            try (Writer writer = new FileWriter(file)) {
                List<Product> products = productRepository.findAll();
                JsonArray jsonArray = new JsonArray();

                for (Product p : products) {
                    JsonObject obj = gson.toJsonTree(p).getAsJsonObject();
                    if (p instanceof Food) {
                        obj.addProperty("type", "food");
                    } else if (p instanceof Drink) {
                        obj.addProperty("type", "drink");
                    }
                    jsonArray.add(obj);
                }
                gson.toJson(jsonArray, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Meniu JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(dashboard.getView().getScene().getWindow());

        if (file != null) {
            try (Reader reader = new FileReader(file)) {
                JsonElement root = JsonParser.parseReader(reader);
                if (root.isJsonArray()) {
                    JsonArray array = root.getAsJsonArray();
                    for (JsonElement element : array) {
                        JsonObject obj = element.getAsJsonObject();
                        String type = obj.has("type") ? obj.get("type").getAsString() : "";

                        Product p = null;
                        if ("food".equalsIgnoreCase(type)) {
                            p = gson.fromJson(obj, Food.class);
                        } else if ("drink".equalsIgnoreCase(type)) {
                            p = gson.fromJson(obj, Drink.class);
                        }

                        if (p != null) {
                            setPrivateId(p, null);
                            productRepository.saveOrUpdate(p);
                        }
                    }
                    menuController.loadData();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPrivateId(Product p, Long id) {
        try {
            Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(p, id);
        } catch (Exception ignored) {
        }
    }
}
