package org.restaurant;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Application;


import com.google.gson.*;
import org.restaurant.GUI.RestaurantApp;
import org.restaurant.config.AppConfig;
import org.restaurant.model.Drink;
import org.restaurant.model.Food;
import org.restaurant.model.Pizza;
import org.restaurant.model.Product;

import static javafx.application.Application.launch;


public class Main {
    public static final String CONFIG_FILE = "config.json";
    public static final String EXPORT_FILE = "exportMenu.json";
    public static final String PRODUCT_FILE = "menu.json";
    public static void alegereOptiuni(Menu.Category cat, TreeMap<Menu.Category, ArrayList<Optional<Product>>> products, Order order) {
        var prods = products.get(cat);
        AtomicInteger index = new AtomicInteger(1);
        for(var prod : prods) {
            prod.ifPresent(value ->
            {System.out.println("Apasati " + index + " pentru a adauga in comanda: " + value.getName()); index.getAndIncrement();});
        }
        System.out.println("Apasati un numar in afara intervalului pentru a iesi din meniul \"" + cat + "\".");

        while(true) {
            var scanner = new Scanner(System.in);
            var choiceIndex = scanner.nextInt();
            if (choiceIndex <= 0 || choiceIndex > prods.size()) {
                System.out.println("Iesire din meniul \"" + cat + "\".");
                return;
            }
            prods.get(choiceIndex - 1).ifPresent(value -> {
                System.out.println("Ati adaugat in comanda: " + value.getName());
                order.addProduct(value, 1);
            });
        }
    }
    public static void buildPizza(Order order) {
        boolean choosing = true;
        double price = 0.0;
        int weight = 0;
        ArrayList<String> ingredients = new ArrayList<>();
        Pizza.Dough dough = null;
        Pizza.Sauce sauce = null;
        var scanner = new Scanner(System.in);
        int choice;

        System.out.println("Apasati 1 pentru a alege blat subtire sau 2 pentru a alege blat pufos.");
        while(choosing) {
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    dough = Pizza.Dough.CRISPY;
                    price += 20.0;
                    weight += 150;
                    choosing = false;
                    break;
                case 2:
                    dough = Pizza.Dough.FLUFFY;
                    price += 20.0;
                    weight += 150;
                    choosing = false;
                    break;
                default:
                    System.out.println("optiune invalida, incercati din nou");
            }
        }
        choosing = true;
        System.out.println("Apasati 1 pentru a adauga sos de rosii");
        System.out.println("Apasati 2 pentru a adauga sos picant");
        System.out.println("Apasati 3 pentru a adauga sos barbecue");
        while(choosing) {
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    sauce = Pizza.Sauce.TOMATO;
                    price += 5.0;
                    weight += 20;
                    choosing = false;
                    break;
                case 2:
                    sauce = Pizza.Sauce.HOT;
                    price += 5.5;
                    weight += 20;
                    choosing = false;
                    break;
                case 3:
                    sauce = Pizza.Sauce.BARBECUE;
                    price += 6.0;
                    weight += 20;
                    choosing = false;
                default:
                    System.out.println("optiune invalida, incercati din nou");
            }
        }
        choosing = true;
        System.out.println("Lista ingrediente:");
        System.out.println("> Mozzarela : 10.0 lei");
        System.out.println("> Cedar : 8.5 lei");
        System.out.println("> Masline : 6.5 lei");
        System.out.println("> Ciuperci : 5.5 lei");
        System.out.println("> Sunca : 6.0 lei");
        System.out.println("> Salam : 5.0 lei");
        System.out.println("> Sardine : 7.0 lei");
        System.out.println("> Ceapa : 3.0 lei");
        System.out.println("> Ardei : 4.0 lei");
        while(choosing) {
            System.out.println("Apasati 1 pentru a adauga mozzarela");
            System.out.println("Apasati 2 pentru a adauga cedar");
            System.out.println("Apasati 3 pentru a adauga masline");
            System.out.println("Apasati 4 pentru a adauga ciuperci");
            System.out.println("Apasati 5 pentru a adauga sunca");
            System.out.println("Apasati 6 pentru a adauga salam");
            System.out.println("Apasati 7 pentru a adauga sardine");
            System.out.println("Apasati 8 pentru a adauga ceapa");
            System.out.println("Apasati 9 pentru a adauga Ardei");
            System.out.println("Apasati orice alta tasta pentru a termina comanda");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Mozzarela adaugata");
                    ingredients.add("mozzarela");
                    price += 10.0;
                    weight += 50;
                    break;
                case 2:
                    System.out.println("Cedar adaugat");
                    ingredients.add("cedar");
                    price += 8.5;
                    weight += 50;
                    break;
                case 3:
                    System.out.println("Masline adaugate");
                    ingredients.add("masline");
                    price += 6.5;
                    weight += 50;
                    break;
                case 4:
                    System.out.println("Ciuperci adaugate");
                    ingredients.add("ciuperci");
                    price += 5.5;
                    weight += 50;
                    break;
                case 5:
                    System.out.println("Sunca adaugata");
                    ingredients.add("sunca");
                    price += 6.0;
                    weight += 50;
                    break;
                case 6:
                    System.out.println("Salam adaugat");
                    ingredients.add("salam");
                    price += 5.0;
                    weight += 50;
                    break;
                case 7:
                    System.out.println("Sardine adaugate");
                    ingredients.add("sardine");
                    price += 7.0;
                    weight += 20;
                    break;
                case 8:
                    System.out.println("Ceapa adaugata");
                    ingredients.add("ceapa");
                    price += 3.0;
                    weight += 20;
                    break;
                case 9:
                    System.out.println("Ardei adaugati");
                    ingredients.add("Ardei");
                    price += 4.0;
                    weight += 50;
                    break;
                default:
                    System.out.println("Comanda incheiata");
                    choosing = false;
            }
        }
        Pizza pizzaCustom = new Pizza.Builder("Pizza custom", price, weight, dough, sauce)
                .addIngredients(ingredients)
                .build();
        order.addPizza(pizzaCustom, 1);
        System.out.println("Pizza adaugata in comanda: " + pizzaCustom.getName() + " - Pret: " + pizzaCustom.getPrice() + " lei");
    }

    private static AppConfig loadConfig() {
        Gson gson = new Gson();
        try (java.io.FileReader reader = new java.io.FileReader(CONFIG_FILE)) {
            return gson.fromJson(reader, AppConfig.class);
        } catch (java.io.IOException e) {
            System.out.println("Eroare la incarcarea fisierului de configurare: " + e.getMessage());
            return null;
        } catch (JsonSyntaxException e) {
            System.out.println("Eroare de sintaxa in fisierul de configurare: " + e.getMessage());
            return null;
        }
    }
    private static TreeMap<Menu.Category, ArrayList<Optional<Product>>> loadMenu() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(PRODUCT_FILE)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            TreeMap<Menu.Category, ArrayList<Optional<Product>>> menu = new TreeMap<>();

            for (Map.Entry<String, JsonElement> entry : root.entrySet()) {
                String catName = entry.getKey();
                Menu.Category category;
                try {
                    category = Menu.Category.valueOf(catName);
                } catch (IllegalArgumentException e) {
                    // skip unknown categories
                    continue;
                }

                ArrayList<Optional<Product>> list = new ArrayList<>();
                JsonArray arr = entry.getValue().getAsJsonArray();
                for (JsonElement el : arr) {
                    JsonObject obj = el.getAsJsonObject();
                    try {
                        String name = obj.has("name") ? obj.get("name").getAsString() : "unnamed";
                        double price = obj.has("price") ? obj.get("price").getAsDouble() : 0.0;

                        Product p;
                        if (obj.has("weight")) {
                            int weight = obj.get("weight").getAsInt();
                            p = new Food(name, price, weight); // assumes Food(String,double,int)
                        } else if (obj.has("volume")) {
                            int volume = obj.get("volume").getAsInt();
                            p = new Drink(name, price, volume); // assumes Drink(String,double,int)
                        } else {
                            throw new Exception("Unknown product type");
                        }
                        list.add(Optional.of(p));
                    } catch (Exception ex) {
                        // malformed item -> add empty optional to keep positions or skip
                        list.add(Optional.empty());
                    }
                }
                menu.put(category, list);
            }

            // ensure all categories exist (optional)
            for (Menu.Category c : Menu.Category.values()) {
                menu.putIfAbsent(c, new ArrayList<>());
            }

            return menu;
        } catch (IOException e) {
            System.out.println("Eroare: `menu.json` lipsește sau nu poate fi citit: " + e.getMessage());
            return null;
        } catch (JsonParseException e) {
            System.out.println("Eroare: `menu.json` este corupt sau nu este JSON valid: " + e.getMessage());
            return null;
        }
    }
    private static void exportMenu(Menu menu) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Map<String, List<Map<String, Object>>> export = new LinkedHashMap<>();

        // iterate categories in the menu (preserves order)
        for (Menu.Category category : menu.getProducts().keySet()) {
            List<Map<String, Object>> items = new ArrayList<>();
            var categoryList = menu.getProducts().getOrDefault(category, new ArrayList<>());
            for (Optional<Product> opt : categoryList) {
                if (opt.isEmpty()) continue;
                Product p = opt.get();
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("name", p.getName());
                item.put("price", p.getPrice());
                // add concrete fields depending on product type
                if (p instanceof Food f) {
                    item.put("type", "Food");
                    item.put("weight", f.getWeight());
                } else if (p instanceof Drink d) {
                    item.put("type", "Drink");
                    item.put("volume", d.getVolume());
                } else {
                    item.put("type", "Unknown");
                }
                items.add(item);
            }
            export.put(category.name(), items);
        }

        try (java.io.FileWriter writer = new java.io.FileWriter(EXPORT_FILE)) {
            gson.toJson(export, writer);
            System.out.println("Meniul a fost exportat cu succes in: " + EXPORT_FILE);
        } catch (java.io.IOException e) {
            System.out.println("Eroare la exportarea meniului: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        AppConfig appConfig = loadConfig();
        if (appConfig == null) {
            System.out.println("Nu s-a putut incarca configuratia aplicatiei. Iesire.");
            return;
        }
        Menu menu = new Menu(loadMenu());
        if (menu.getProducts().isEmpty()) {
            System.out.println("Meniul este gol sau nu a putut fi incarcat. Iesire.");
            return;
        }
        exportMenu(menu);
        Order order = new Order();
        order.setTVA(appConfig.getTVA());
        var scanner = new Scanner(System.in);
        System.out.println("Selectati 1 pentru a incepe comanda");
        System.out.println("Selectati 2 pentru a cauta un produs dupa nume");
        System.out.println("Selectati 3 pentru a vedea rapoartele meniului");
        System.out.println("Selectati orice alta tasta pentru a iesi din aplicatie");

        Application.launch(RestaurantApp.class, args);
//        while (true) {
//            var initialChoice = scanner.nextLine();
//            switch (initialChoice) {
//                case "1":
//                    while (true) {
//                        System.out.println("--- Meniul Restaurantului" +appConfig.getRestaurantName() +"---");
//                        menu.showCategory(Menu.Category.APERITIVE);
//                        menu.showCategory(Menu.Category.FEL_PRINCIPAL);
//                        menu.showCategory(Menu.Category.DESERT);
//                        menu.showCategory(Menu.Category.BAUTURI_RACORITOARE);
//                        menu.showCategory(Menu.Category.BAUTURI_ALCOOLICE);
//                        menu.showCategory(Menu.Category.ALTE_BAUTURI);
//                        menu.showCategory(Menu.Category.PRODUSE_VEGETARIENE);
//                        System.out.println("----------------------------------------");
//                        System.out.println("Apasati tasta 0 pentru a finaliza comanda");
//                        System.out.println("Apasati tasta 1 pentru a selecta un fel principal");
//                        System.out.println("Apasati tasta 2 pentru a selecta un aperitiv");
//                        System.out.println("Apasati tasta 3 pentru a selecta un desert");
//                        System.out.println("Apasati tasta 4 pentru a selecta o bautura racoritoare");
//                        System.out.println("Apasati tasta 5 pentru a selecta o bautura alcoolica");
//                        System.out.println("Apasati tasta 6 pentru a selecta un alt tip de bautura");
//                        System.out.println("Apasati tasta 7 pentru a comanda un produs vegetarian");
//                        System.out.println("Apasati tasta 8 pentru a comanda o pizza customizabila");
//                        System.out.println("Apasati orice alta tasta pentru a iesi din aplicatie");
//                        var choice = scanner.nextLine();
//                        switch (choice) {
//                            case "0":
//                                if (order.products.isEmpty() && order.pizzas.isEmpty()) {
//                                    System.out.println("Nu ati comandat nimic.");
//                                    break;
//                                }
//                                double total = order.getTotalWithDiscount(order.HappyHourDiscount, 0.2);
//                                System.out.println("Total de plata (cu TVA): " + total);
//
//                                System.out.println("Va multumim pentru comanda!");
//                                order.products.clear();
//                                return;
//                            case "1":
//                                alegereOptiuni(Menu.Category.FEL_PRINCIPAL, menu.getProducts(), order);
//                                break;
//                            case "2":
//                                alegereOptiuni(Menu.Category.APERITIVE, menu.getProducts(), order);
//                                break;
//                            case "3":
//                                alegereOptiuni(Menu.Category.DESERT, menu.getProducts(), order);
//                                break;
//                            case "4":
//                                alegereOptiuni(Menu.Category.BAUTURI_RACORITOARE, menu.getProducts(), order);
//                                break;
//                            case "5":
//                                alegereOptiuni(Menu.Category.BAUTURI_ALCOOLICE, menu.getProducts(), order);
//                                break;
//                            case "6":
//                                alegereOptiuni(Menu.Category.ALTE_BAUTURI, menu.getProducts(), order);
//                                break;
//                            case "7":
//                                alegereOptiuni(Menu.Category.PRODUSE_VEGETARIENE, menu.getProducts(), order);
//                                break;
//                            case "8":
//                                buildPizza(order);
//                                break;
//                            default:
//                                System.out.println("Iesire din aplicatie");
//                                return;
//                        }
//                    }
//                case "2":
//                    while (true) {
//                        System.out.println("Introduceti numele produsului pe care doriti sa il cautati sau apasati tasta 0 pentru a iesi din functia de cautare:");
//                        String input = scanner.nextLine();
//                        switch (input) {
//                            case "0":
//                                System.out.println("Iesire din functia de cautare.");
//                                return;
//                            default:
//                                menu.searchProductByName(input);
//                        }
//                    }
//                case "3":
//                    System.out.println("Pretul mediu al deserturilor este: ");
//                    OptionalDouble medieFelPrincipal = menu.getProducts().get(Menu.Category.DESERT).stream()
//                            .filter(Optional::isPresent)
//                            .mapToDouble(prod -> prod.get().getPrice())
//                            .average();
//                    medieFelPrincipal.ifPresentOrElse(
//                            avg -> System.out.println(avg + " lei\n"),
//                            () -> System.out.println("Nu exista produse in aceasta categorie.\n")
//                    );
//                    ArrayList<Product> produseScumpe = menu.getProducts().values().stream()
//                            .flatMap(List::stream)
//                            .filter(Optional::isPresent)
//                            .map(Optional::get)
//                            .filter(prod -> prod.getPrice() > 100.0)
//                            .collect(Collectors.toCollection(ArrayList::new));
//                    if (produseScumpe.isEmpty()) {
//                        System.out.println("Nu exista produse care sa coste mai mult de 100 lei.\n");
//                    } else {
//                        System.out.println("Produse care costa mai mult decat 100 lei:");
//                        for (Product prod : produseScumpe) {
//                            System.out.println(prod.getName() + " - " + prod.getPrice() + " lei");
//                        }
//                        System.out.println();
//                    }
//                    System.out.println("Produse vegetariene in ordine alfabetica:");
//                    ArrayList<Product> produseVegetariene = menu.getProducts().get(Menu.Category.PRODUSE_VEGETARIENE).stream()
//                            .filter(Optional::isPresent)
//                            .map(Optional::get)
//                            .sorted(Comparator.comparing(Product::getName))
//                            .collect(Collectors.toCollection(ArrayList::new));
//                    for (Product prod : produseVegetariene) {
//                        System.out.println(prod.getName() + " - " + prod.getPrice() + " lei");
//                    }
//                    break;
//                default:
//                    System.out.println("Inchiderea aplicatiei.");
//            }
//        }
    }
}