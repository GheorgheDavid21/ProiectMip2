module org.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires jdk.xml.dom;
    requires javafx.graphics;
    requires javafx.base;
//    requires org.restaurant;


    opens org.restaurant to javafx.graphics, javafx.fxml, com.google.gson;
    exports org.restaurant;
    exports org.restaurant.lambda;
    opens org.restaurant.lambda to javafx.fxml, javafx.graphics, com.google.gson;
    exports org.restaurant.model;
    opens org.restaurant.model to javafx.fxml, javafx.graphics, com.google.gson;
    exports org.restaurant.config;
    opens org.restaurant.config to javafx.fxml, javafx.graphics, com.google.gson;
    opens org.restaurant.GUI to javafx.fxml, javafx.graphics, com.google.gson;
    exports org.restaurant.GUI;
}