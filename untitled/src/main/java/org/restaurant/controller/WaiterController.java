package org.restaurant.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.restaurant.GUI.RestaurantApplication;
import org.restaurant.GUI.WaiterDashboard;
import org.restaurant.mapper.ProductMapper;
import org.restaurant.model.*;
import org.restaurant.persistence.OrderRepository;
import org.restaurant.persistence.ProductRepository;
import org.restaurant.view.OrderItemView;
import org.restaurant.view.ProductProfile;

import java.util.*;
import java.util.stream.Collectors;

public class WaiterController {
    private final WaiterDashboard view;
    private final ProductRepository productRepository;
    private final RestaurantApplication app;
    private final OrderRepository orderRepository;
    private final Order currentOrder = new Order();

    private final ObservableList<ProductProfile> masterList = FXCollections.observableArrayList();

    public WaiterController(WaiterDashboard view, ProductRepository productRepository, RestaurantApplication app, OrderRepository orderRepository) {
        this.view = view;
        this.productRepository = productRepository;
        this.app = app;
        this.orderRepository = orderRepository;

        initData();
        initEvents();
    }

    private void initData() {
        List<Product> products = productRepository.findAll();
        List<ProductProfile> profiles = products.stream()
                .map(ProductMapper::toProfile)
                .toList();
        masterList.setAll(profiles);
        view.getMenuTable().setItems(masterList);
    }

    private void initEvents() {
        view.getMenuTable().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            displayProductDetails(newVal);
        });

        view.getAddBtn().setOnAction(e -> {
            if (view.getMenuTable().getSelectionModel().getSelectedItem() == null) {
                return;
            }
            Product p = view.getMenuTable().getSelectionModel().getSelectedItem().getProduct();
            if (p != null) {
                addToCart(p);
            }
        });

        view.getRemoveBtn().setOnAction(e -> {
            OrderItemView selected = view.getCartList().getSelectionModel().getSelectedItem();
            if (selected != null && selected.getOrderItem().getProduct() != null) {
                removeFromCart(selected.getOrderItem().getProduct());
            }
        });

        view.getPlaceOrderBtn().setOnAction(e -> placeOrder());
        view.getHistTab().setOnSelectionChanged(e -> {
            if (view.getHistTab().isSelected()) {
                view.getHistoryController().refreshData();
            }
        });
    }

    private void addToCart(Product product) {
        if (product != null) {
            List<OrderItemView> existingItems = view.getCartList().getItems().stream()
                    .filter(itemView -> itemView.getOrderItem().getProduct() != null &&
                            Objects.equals(itemView.getOrderItem().getProduct().getId(), product.getId()) &&
                            itemView.getOrderItem().getPriceAtOrder() >= 0) // Ensure it's not a discount item
                    .toList();

            if (!existingItems.isEmpty()) {
                OrderItemView existingItemView = existingItems.getFirst();
                existingItemView.getOrderItem().addQuantity(1);
                existingItemView.updateView();
            } else {
                // Create real order item
                OrderItem newOrderItem = new OrderItem(currentOrder, product, 1);
                currentOrder.addItem(newOrderItem);
                OrderItemView newOrderItemView = new OrderItemView(newOrderItem);
                view.getCartList().getItems().add(newOrderItemView);
            }
            applyOffers();
        }
    }

    private void removeFromCart(Product product) {
        if (product != null) {
            List<OrderItemView> existingItems = view.getCartList().getItems().stream()
                    .filter(itemView -> itemView.getOrderItem().getProduct() != null &&
                            Objects.equals(itemView.getOrderItem().getProduct().getId(), product.getId()) &&
                            itemView.getOrderItem().getPriceAtOrder() >= 0)
                    .toList();

            if (!existingItems.isEmpty()) {
                OrderItemView existingItemView = existingItems.getFirst();
                existingItemView.getOrderItem().addQuantity(-1);
                if (existingItemView.getOrderItem().getQuantity() <= 0) {
                    currentOrder.removeItem(existingItemView.getOrderItem());
                    view.getCartList().getItems().remove(existingItemView);
                } else {
                    existingItemView.updateView();
                }
                applyOffers();
            }
        }
    }

    private void applyOffers() {
        view.getCartList().getItems().removeIf(ov -> ov.getOrderItem().getPriceAtOrder() < 0);

        List<OrderItem> realItems = currentOrder.getItems();
        List<OrderItem> discountItems = new ArrayList<>();


        if (OffersController.HAPPY_HOUR_ACTIVE) {
            List<Product> drinks = new ArrayList<>();
            for (OrderItem item : realItems) {
                if (item.getProduct() instanceof Drink) {
                    for (int i = 0; i < item.getQuantity(); i++) drinks.add(item.getProduct());
                }
            }

            drinks.sort(Comparator.comparingDouble(Product::getPrice));
            int discountCount = drinks.size() / 2;
            for (int i = 0; i < discountCount; i++) {
                Product d = drinks.get(i);
                createDiscountItem(discountItems, "HappyHour(-50%): " + d.getName(), -d.getPrice() * 0.5);
            }
        }

        if (OffersController.MEAL_DEAL_ACTIVE) {
            List<Product> pizzas = expandProducts(realItems, "Pizza", false);
            List<Product> desserts = expandProducts(realItems, "Desert", false);

            int pairs = Math.min(pizzas.size(), desserts.size());
            desserts.sort(Comparator.comparingDouble(Product::getPrice));

            for (int i = 0; i < pairs; i++) {
                Product d = desserts.get(i);
                createDiscountItem(discountItems, "MealDeal(-25%): " + d.getName(), -d.getPrice() * 0.25);
            }
        }

        if (OffersController.PARTY_PACK_ACTIVE) {
            List<Product> pizzas = expandProducts(realItems, "Pizza", true);
            pizzas.sort(Comparator.comparingDouble(Product::getPrice));

            int freePizzas = pizzas.size() / 4;
            for (int i = 0; i < freePizzas; i++) {
                Product p = pizzas.get(i);
                createDiscountItem(discountItems, "PartyPack(Free): " + p.getName(), -p.getPrice());
            }
        }

        for (OrderItem di : discountItems) {
            view.getCartList().getItems().add(new OrderItemView(di));
        }

        updateTotalLabel();
    }

    private void createDiscountItem(List<OrderItem> list, String name, double value) {
        Product discountP = new Food(name, 0, "Oferta", 0);
        OrderItem di = new OrderItem(null, discountP, 1);
        discountP.setPrice(value);
        Product negativeProduct = new Food(name, value, "Oferta", 0);
        OrderItem item = new OrderItem(currentOrder, negativeProduct, 1);

        list.add(item);
    }

    private List<Product> expandProducts(List<OrderItem> items, String keyword, boolean strict) {
        List<Product> expanded = new ArrayList<>();
        for (OrderItem oi : items) {
            Product p = oi.getProduct();
            boolean match = false;
            if (p.getName().contains(keyword) || (p.getCategory() != null && p.getCategory().contains(keyword))) {
                match = true;
            }
            if (match) {
                for (int i = 0; i < oi.getQuantity(); i++) expanded.add(p);
            }
        }
        return expanded;
    }

    private void updateTotalLabel() {
        double total = view.getCartList().getItems().stream()
                .mapToDouble(v -> v.getOrderItem().getProduct().getPrice() * v.getQuantity()) // visual sum
                .sum();
        view.getTotalLabel().setText("Total: " + String.format("%.2f RON", total));
    }

    private void placeOrder() {
        int tableNumber = view.getTableSpinner().getValue();
        List<OrderItem> visualItems = view.getCartList().getItems().stream()
                .map(OrderItemView::getOrderItem)
                .toList();



        double totalDiscount = 0.0;
        double finalTotal = 0.0;

        currentOrder.setTableNumber(tableNumber);
        currentOrder.setUser(view.getLoggedUser());


        for (OrderItemView v : view.getCartList().getItems()) {
            double lineTotal = v.getOrderItem().getProduct().getPrice() * v.getQuantity();
            if (lineTotal < 0) {
                totalDiscount += Math.abs(lineTotal);
            } else {
                finalTotal += lineTotal;
            }
        }

        currentOrder.setTotalAmount(Math.max(0, finalTotal - totalDiscount));
        currentOrder.setDiscountAmount(totalDiscount);

        if (!currentOrder.getItems().isEmpty()) {
            orderRepository.save(currentOrder);
        }

        currentOrder.getItems().clear();
        view.getCartList().getItems().clear();
        view.getTotalLabel().setText("Total: 0.0 RON");

    }

    private void displayProductDetails(ProductProfile profile) {
        if (profile != null) {
            view.getDetName().setText(profile.getName().get());
            view.getDetCategory().setText(profile.getCategoryName().get());
            view.getDetPrice().setText(String.format("%.2f RON", profile.getPrice().get()));
            view.getDetMeasure().setText(profile.getMeasure().get());
        } else {
            view.getDetName().clear();
            view.getDetCategory().clear();
            view.getDetPrice().clear();
            view.getDetMeasure().clear();
        }
    }

}
