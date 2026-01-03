package org.restaurant.mapper;

import org.restaurant.model.Drink;
import org.restaurant.model.Food;
import org.restaurant.model.Product;
import org.restaurant.view.ProductProfile;

public class ProductMapper {
    public static ProductProfile toProfile(Product entity) {
        if (entity == null) {
            return null;
        }
        return new ProductProfile(entity);
    }

    public static Product toEntity(ProductProfile profile) {
        if(profile == null) {
            return null;
        }
        String name = profile.getName().get();
        double price = profile.getPrice().get();
        String category = profile.getCategoryName().get();
        String measureStr = profile.getMeasure().get();

        Product product;

        int measure = 0;
        if (measureStr != null && measureStr.endsWith(" ml")){
            try {
                measure = Integer.parseInt(measureStr.replace(" ml", "").trim());
            } catch (NumberFormatException ignored) {}
            product = new Drink(name, price, category, measure);
        } else if(measureStr.endsWith(" g")){
            try {
                measure = Integer.parseInt(measureStr.replace(" g", "").trim());
            } catch (NumberFormatException ignored) {}
            product = new Food(name, price, category, measure);
        }
        else {
            return null;
        }
        return product;
    }
}
