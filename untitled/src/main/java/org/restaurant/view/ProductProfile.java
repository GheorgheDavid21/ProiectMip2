package org.restaurant.view;

import javafx.beans.property.*;
import org.restaurant.model.Drink;
import org.restaurant.model.Food;
import org.restaurant.model.Product;

public class ProductProfile {
//    private final Product product;
//    private final MenuProducts.Category category;
//    private final boolean isFood;
    private final Product product;

    private final StringProperty name = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final StringProperty categoryName = new SimpleStringProperty();
    private final StringProperty measure = new SimpleStringProperty();

    public ProductProfile(Product product) {
        this.product = product;
        this.name.set(product.getName());
        this.price.set(product.getPrice());
        this.categoryName.set(product.getCategory());

        if (product instanceof Food f) {
            this.measure.set(f.getWeight() + " g");
        } else if (product instanceof Drink d) {
            this.measure.set(d.getVolume() + " ml");
        } else {
            this.measure.set("-");
        }
        this.price.addListener((obs, oldV, newV) -> {
            double v = newV == null ? 0.0 : newV.doubleValue();
            product.setPrice(v);
        });
    }

    public Product getProduct() {
        return product;
    }
    public StringProperty getName() {
        return name;
    }
    public DoubleProperty getPrice() {
        return price;
    }
    public StringProperty getMeasure() {
        return measure;
    }
    public StringProperty getCategoryName() {
        return categoryName;
    }

}
