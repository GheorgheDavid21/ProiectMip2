package org.restaurant.controller;

import com.google.gson.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;
import org.restaurant.GUI.MenuView;

import org.restaurant.mapper.ProductMapper;
import org.restaurant.model.Drink;
import org.restaurant.model.Food;
import org.restaurant.model.Product;
import org.restaurant.persistence.ProductRepository;
import org.restaurant.view.ProductProfile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MenuController {
    private final MenuView view;
    private final ProductRepository productRepository;
    private final ObservableList<ProductProfile> products;

    private ChangeListener<Boolean> currentPriceFocusListener;

    public MenuController(MenuView view, ProductRepository productRepository) {
        this.view = view;
        this.productRepository = productRepository;
        this.products = FXCollections.observableArrayList();

        view.getProductList().setItems(products);

        view.getNameField().setEditable(false);
        view.getCategoryField().setEditable(false);
        view.getMeasureField().setEditable(false);

        initEvents();
        loadData();

    }

    private void initEvents() {

        view.getProductList().getSelectionModel().selectedItemProperty()
                .addListener((obs,
                              oldVal,
                              newVal) -> {
            if (oldVal != null) {
                unbindDetails(oldVal);
            }
            if (newVal != null) {
                bindDetails(newVal);
            } else {
                clearDetails();
            }
        });

        view.getExportMenuItem().setOnAction(e -> handleExport());
        view.getImportMenuItem().setOnAction(e -> handleImport());
    }

    private void handleExport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Menu to JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(view.getView().getScene().getWindow());

        if (file != null) {
            exportToJson(file);
        }
    }

    private void handleImport() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Menu from JSON");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(view.getView().getScene().getWindow());

        if (file != null) {
            importFromJson(file);
            loadData();
        }
    }

    private void exportToJson(File file) {
        List<Product> allProducts = productRepository.findAll();

        Map<String, List<Map<String, Object>>> exportMap = new LinkedHashMap<>();

        for (Product p : allProducts) {
            exportMap.putIfAbsent(p.getCategory(), new ArrayList<>());
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("name", p.getName());
            item.put("price", p.getPrice());

            if (p instanceof Food f) {
                item.put("type", "Food");
                item.put("weight", f.getWeight());
            } else if (p instanceof Drink d) {
                item.put("type", "Drink");
                item.put("volume", d.getVolume());
            }
            exportMap.get(p.getCategory()).add(item);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(exportMap, writer);
            System.out.println("Export successful to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void importFromJson(File file) {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(file)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                String category = entry.getKey();
                JsonArray items = entry.getValue().getAsJsonArray();
                for (JsonElement el : items) {
                    JsonObject obj = el.getAsJsonObject();
                    try {
                        String name = obj.get("name").getAsString();
                        double price = obj.get("price").getAsDouble();
                        Product p = null;

                        if (obj.has("weight")) {
                            p = new Food(name, price, category, obj.get("weight").getAsInt());
                        } else if (obj.has("volume")) {
                            p = new Drink(name, price, category, obj.get("volume").getAsInt());
                        }

                        if (p != null) {
                            productRepository.saveOrUpdate(p);
                        }
                    } catch (Exception ex) {
                        System.out.println("Skipping invalid item in import: " + ex.getMessage());
                    }
                }
            }
            System.out.println("Import successful from " + file.getAbsolutePath());
        } catch (IOException | JsonParseException e) {
            e.printStackTrace();
        }
    }

    private void bindDetails(ProductProfile profile) {
        view.getNameField().setText(profile.getName().get());
//        view.getPriceField().textProperty().bindBidirectional(profile.getPrice(), new NumberStringConverter());
        view.getCategoryField().setText(profile.getCategoryName().get());
        view.getMeasureField().setText(profile.getMeasure().get());

        Bindings.bindBidirectional(
                view.getPriceField().textProperty(),
                profile.getPrice(),
                new NumberStringConverter()
        );

        currentPriceFocusListener = (obs, wasFocused, isFocused) -> {
            if (!isFocused) {
                saveProduct(profile);
            }
        };
        view.getPriceField().focusedProperty().addListener(currentPriceFocusListener);

        view.getPriceField().setOnAction(e ->{
            saveProduct(profile);
            view.getPriceField().requestFocus();
        });

    }

    private void unbindDetails(ProductProfile profile) {

        saveProduct(profile);

//        view.getNameField().textProperty().unbindBidirectional(profile.getName());
//        view.getCategoryField().textProperty().unbindBidirectional(profile.getCategoryName());
//        view.getMeasureField().textProperty().unbindBidirectional(profile.getMeasure());

        Bindings.unbindBidirectional(view.getPriceField().textProperty(), profile.getPrice());

        if (currentPriceFocusListener != null) {
            view.getPriceField().focusedProperty().removeListener(currentPriceFocusListener);
            currentPriceFocusListener = null;
        }

//        view.getPriceField().clear();
        view.getPriceField().setOnAction(null);
    }

    private void saveProduct(ProductProfile profile) {
        Product p = profile.getProduct();
        productRepository.saveOrUpdate(p);
        System.out.println("Produs salvat: " + p.getName() + " -> " + p.getPrice());
    }

    private void clearDetails() {
        view.getNameField().clear();
        view.getPriceField().clear();
        view.getCategoryField().clear();
        view.getMeasureField().clear();
    }

    private void loadData() {
        List<Product> entities = productRepository.findAll();
        List<ProductProfile> profiles = entities.stream()
                .map(ProductMapper::toProfile)
                .collect(Collectors.toList());
        products.setAll(profiles);

        if (!products.isEmpty()) {
            view.getProductList().getSelectionModel().selectFirst();
        }
    }
}
