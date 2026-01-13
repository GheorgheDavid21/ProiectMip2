package org.restaurant.controller;

import com.google.gson.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;
import org.restaurant.GUI.GuestView;

import org.restaurant.GUI.ProductDialog;
import org.restaurant.GUI.RestaurantApplication;
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
import java.util.*;
import java.util.stream.Collectors;

public class MenuController {
    private final GuestView view;
    private final ProductRepository productRepository;
    private final ObservableList<ProductProfile> products;
    private final RestaurantApplication app;
    private final ObservableList<ProductProfile> masterList = FXCollections.observableArrayList();
    private final ProductDialog productDialog;

    public MenuController(GuestView view, ProductRepository productRepository, RestaurantApplication app, ProductDialog productDialog) {
        this.view = view;
        this.productRepository = productRepository;
        this.app = app;
        this.productDialog = productDialog;
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
        view.getSearchField().textProperty().addListener((obs, oldV, newV) -> applyFilters());
        view.getTypeFilter().valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        view.getVegFilter().selectedProperty().addListener((obs, oldV, newV) -> applyFilters());
        view.getMinPriceField().textProperty().addListener((obs, oldV, newV) -> applyFilters());
        view.getMaxPriceField().textProperty().addListener((obs, oldV, newV) -> applyFilters());

        view.getResetBtn().setOnAction(e -> resetFilters());
        view.getLoginButton().setOnAction(e -> app.showGuestView());
        productDialog.setResultConverter(dialogButton -> {
            if (dialogButton == productDialog.getSaveButtonType()) {
                try {
                    String name = productDialog.getNameField().getText();
                    double price = Double.parseDouble(productDialog.getPriceField().getText());
                    String category = productDialog.getCategoryField().getText();
                    int measure = Integer.parseInt(productDialog.getMeasureField().getText());

                    if (productDialog.getTypeCombo().getValue().equals("Mancare")) {
                        return new Food(name, price, category, measure);
                    } else {
                        return new Drink(name, price, category, measure);
                    }
                } catch (Exception ex) {
                    return null;
                }
            }
            return null;
        });
    }

    private void resetFilters() {
        view.getSearchField().clear();
        view.getTypeFilter().getSelectionModel().selectFirst();
        view.getVegFilter().setSelected(false);
        view.getMinPriceField().clear();
        view.getMaxPriceField().clear();
    }

    private void applyFilters() {
        String searchText = view.getSearchField().getText().toLowerCase().trim();
        String selectedType = view.getTypeFilter().getValue();
        boolean isVegOnly = view.getVegFilter().isSelected();
        Double minPrice = parseDoubleSafely(view.getMinPriceField().getText());
        Double maxPrice = parseDoubleSafely(view.getMaxPriceField().getText());

        List<ProductProfile> filtered = masterList.stream()
                .filter(p -> Optional.ofNullable(p.getName().get())
                        .map(String::toLowerCase)
                        .map(name -> name.contains(searchText))
                        .orElse(false))
                .filter(p -> {
                    if ("Mancare".equals(selectedType)) return p.getProduct() instanceof Food;
                    if ("Bautura".equals(selectedType)) return p.getProduct() instanceof Drink;
                    return true;
                })
                .filter(p -> {
                    if (isVegOnly) {
                        return "PRODUSE_VEGETARIENE".equalsIgnoreCase(p.getCategoryName().get());
                    }
                    return true;
                })
                .filter(p -> {
                    double price = p.getPrice().get();
                    if (minPrice != null && price < minPrice) return false;
                    if (maxPrice != null && price > maxPrice) return false;
                    return true;
                })
                .collect(Collectors.toList());

        products.setAll(filtered);
    }

    private Double parseDoubleSafely(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void enableManagerMode() {
        view.getNameField().setEditable(true);
        view.getCategoryField().setEditable(true);
        view.getMeasureField().setEditable(true);
        view.getPriceField().setEditable(true);
        view.getSaveDetailsBtn().setVisible(true);
    }

    public void addNewProduct() {
        productDialog.getNameField().clear();
        productDialog.getPriceField().clear();
        productDialog.getCategoryField().clear();
        productDialog.getMeasureField().clear();

        if (productDialog.getTypeCombo() != null && !productDialog.getTypeCombo().getItems().isEmpty()) {
            productDialog.getTypeCombo().getSelectionModel().selectFirst();
        }

        Optional<Product> result = productDialog.showAndWait();

        result.ifPresent(product -> {
            ProductProfile profile = ProductMapper.toProfile(product);
            saveProduct(profile);
            loadData();
        });
    }

    public void deleteSelectedProduct() {
        ProductProfile selected = view.getProductList().getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Stergi produsul " + selected.getName().get() + "?", ButtonType.YES, ButtonType.NO);
        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            productRepository.delete(selected.getProduct());
            loadData();
        }
    }



    private void bindDetails(ProductProfile profile) {
        view.getNameField().setText(profile.getName().get());
        view.getCategoryField().setText(profile.getCategoryName().get());
        view.getMeasureField().setText(profile.getMeasure().get());
        view.getPriceField().setText(String.format("%.2f", profile.getPrice().get()));

        view.getSaveDetailsBtn().setOnAction(e -> {
            applyChanges(profile);
        });

    }

    private void applyChanges(ProductProfile profile) {
        String name = view.getNameField().getText();
        String catStr = view.getCategoryField().getText();
        String priceStr = view.getPriceField().getText();

        profile.getName().set(name);
        profile.getProduct().setName(name);

        profile.getCategoryName().set(catStr);
        profile.getProduct().setCategory(catStr);

        try {
            String cleanPrice = priceStr.replace("RON", "").replace("ron", "").trim();
            double price = Double.parseDouble(cleanPrice);
            profile.getPrice().set(price);
        } catch (NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Pret invalid!");
            a.show();
            return;
        }

        saveProduct(profile);

        Alert success = new Alert(Alert.AlertType.INFORMATION, "Produs salvat cu succes.");
        success.show();
    }

    private void unbindDetails(ProductProfile profile) {

        view.getSaveDetailsBtn().setOnAction(null);
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
        view.getSaveDetailsBtn().setOnAction(null);
    }

    public void loadData() {
        List<Product> entities = productRepository.findAll();
        List<ProductProfile> profiles = entities.stream()
                .map(ProductMapper::toProfile)
                .collect(Collectors.toList());
        products.setAll(profiles);

        masterList.setAll(profiles);
        applyFilters();

        if (!products.isEmpty()) {
            view.getProductList().getSelectionModel().selectFirst();
        }
    }
}
