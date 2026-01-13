module org.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires jdk.xml.dom;
    requires javafx.graphics;
    requires javafx.base;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires org.postgresql.jdbc;

    uses jakarta.persistence.spi.PersistenceProvider;

    exports org.restaurant.lambda;
    opens org.restaurant.lambda to javafx.fxml, javafx.graphics, com.google.gson;
    exports org.restaurant.model;
    opens org.restaurant.model to javafx.fxml, javafx.graphics, com.google.gson, org.hibernate.orm.core;
    exports org.restaurant.config;
    opens org.restaurant.config to javafx.fxml, javafx.graphics, com.google.gson;
    opens org.restaurant.GUI to javafx.fxml, javafx.graphics, com.google.gson;
    exports org.restaurant.GUI;
    exports org.restaurant.view;
    opens org.restaurant.view to javafx.fxml, javafx.graphics, com.google.gson;
    exports org.restaurant.controller;
    opens org.restaurant.controller to javafx.fxml, javafx.graphics, com.google.gson;
    exports org.restaurant.service;
    opens org.restaurant.service to javafx.fxml, javafx.graphics, com.google.gson;
}