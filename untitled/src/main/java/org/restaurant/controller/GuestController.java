package org.restaurant.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.restaurant.GUI.GuestView;
import org.restaurant.GUI.RestaurantApplication;
import org.restaurant.mapper.ProductMapper;
import org.restaurant.model.Drink;
import org.restaurant.model.Food;
import org.restaurant.model.Product;
import org.restaurant.persistence.ProductRepository;
import org.restaurant.view.ProductProfile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GuestController {

    private final GuestView view;
    private final ProductRepository productRepository;
    private final RestaurantApplication app;

    private final ObservableList<ProductProfile> masterList = FXCollections.observableArrayList();
    private final ObservableList<ProductProfile> displayedList = FXCollections.observableArrayList();

    ;

    public GuestController(GuestView view, ProductRepository productRepository, RestaurantApplication app) {
        this.view = view;
        this.productRepository = productRepository;
        this.app = app;

        initData();
        initEvents();
    }

    private void initData() {
        List<Product> products = productRepository.findAll();

        List<ProductProfile> profiles = products.stream()
                .map(ProductMapper::toProfile)
                .collect(Collectors.toList());

        masterList.setAll(profiles);
        displayedList.setAll(profiles);

        view.getProductList().setItems(displayedList);
    }

    private void initEvents() {
        view.getProductList().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            displayDetails(newVal);
        });

        view.getSearchField().textProperty().addListener((obs, oldV, newV) -> applyFilters());

        view.getLoginButton().setOnAction(e -> app.showLoginView());

        view.getResetBtn().setOnAction(e -> resetFilters());

        view.getTypeFilter().valueProperty().addListener((obs, oldV, newV) -> applyFilters());
        view.getVegFilter().selectedProperty().addListener((obs, oldV, newV) -> applyFilters());
        view.getMinPriceField().textProperty().addListener((obs, oldV, newV) -> applyFilters());
        view.getMaxPriceField().textProperty().addListener((obs, oldV, newV) -> applyFilters());
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

        // Filter using Streams API
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

        displayedList.setAll(filtered);
    }

    private void displayDetails(ProductProfile profile) {
        if (profile == null) {
            view.getNameField().clear();
            view.getCategoryField().clear();
            view.getMeasureField().clear();
            view.getPriceField().clear();
        } else {
            view.getNameField().setText(profile.getName().get());
            view.getCategoryField().setText(profile.getCategoryName().get());
            view.getMeasureField().setText(profile.getMeasure().get());
            view.getPriceField().setText(String.format("%.2f RON", profile.getPrice().get()));
        }
    }

    private Double parseDoubleSafely(String text) {
        if (text == null || text.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}