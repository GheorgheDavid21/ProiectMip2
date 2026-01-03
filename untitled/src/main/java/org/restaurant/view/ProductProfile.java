package org.restaurant.view;

import javafx.beans.property.*;
import org.restaurant.Menu;
import org.restaurant.model.Drink;
import org.restaurant.model.Food;
import org.restaurant.model.Product;

public class ProductProfile {
    private final Product product;
    private final Menu.Category category;
    private final boolean isFood;


    private final StringProperty name = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final StringProperty categoryName = new SimpleStringProperty();
    private final IntegerProperty measure = new SimpleIntegerProperty();

    public ProductProfile(Product product, Menu.Category category) {
        this.product = product;
        this.category = category;
        this.name.set(product.getName());
        this.price.set(product.getPrice());
        this.categoryName.set(category.name());

        if (product instanceof Food f) {
            this.measure.set(f.getWeight());
            this.isFood = true;
        } else if (product instanceof Drink d) {
            this.measure.set(d.getVolume());
            this.isFood = false;
        } else {
            this.measure.set(0);
            this.isFood = true;
        }
        this.price.addListener((obs, oldV, newV) -> {
            double v = newV == null ? 0.0 : newV.doubleValue();
            if (product instanceof Food f) {
                f.setPrice(v);
            } else if (product instanceof Drink d) {
                d.setPrice(v);
            }
        });
    }

    public StringProperty getName() {
        return name;
    }
    public DoubleProperty getPrice() {
        return price;
    }
    public Menu.Category getCategory() {
        return category;
    }
    public IntegerProperty getMeasure() {
        return measure;
    }
    public boolean getIsFood() {
        return isFood;
    }
    public StringProperty getCategoryName() {
        return categoryName;
    }

}
