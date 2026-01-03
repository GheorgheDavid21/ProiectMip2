package org.restaurant;

import org.restaurant.model.Product;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Menu {
    public enum Category {
        APERITIVE,
        FEL_PRINCIPAL,
        DESERT,
        BAUTURI_RACORITOARE,
        BAUTURI_ALCOOLICE,
        ALTE_BAUTURI,
        PRODUSE_VEGETARIENE
    }
    private final TreeMap<Category, ArrayList<Optional<Product>>> products = new TreeMap<>();


    public Menu() {
    }
    public Menu(TreeMap<Category, ArrayList<Optional<Product>>> products) {
        this.products.putAll(products);
    }
    public TreeMap<Category, ArrayList<Optional<Product>>> getProducts() {
        return products;
    }

    public void showCategory(Category category){
        System.out.println("---- " + category + " ----");
        ArrayList<Optional<Product>> categoryProducts = products.get(category);
        if(categoryProducts != null){
            for(Optional<Product> productOpt : categoryProducts){
                productOpt.ifPresent(product -> System.out.println(product.toString()));
            }
        } else {
            System.out.println("No products available in this category.");
        }
    }
    public void addProduct(Category category, Product product) {
        products.putIfAbsent(category, new ArrayList<>());
        products.get(category).add(Optional.of(product));
    }

    public void searchProductByName(String name){
        AtomicBoolean found = new AtomicBoolean(false);
        System.out.println("---- Search Results for: " + name + " ----");
        for(Category category : products.keySet()){
            ArrayList<Optional<Product>> categoryProducts = products.get(category);
            for(Optional<Product> productOpt : categoryProducts){
                productOpt.ifPresent(product -> {
                    if(product.getName().equalsIgnoreCase(name)){
                        System.out.println(product.toString() + " (Category: " + category + ")");
                        found.set(true);
                    }
                });
            }
        }
        if(!found.get()){
            System.out.println("No products found with the name: " + name);
        }
    }

}

