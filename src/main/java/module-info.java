module com.codingfactory.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.apache.commons.lang3;
    requires mongo.java.driver;
    requires java.dotenv;
    requires freemarker;
    requires org.jsoup;
    requires openhtmltopdf.core;
    requires openhtmltopdf.pdfbox;

    opens com.codingfactory.restaurant to javafx.fxml;
    opens com.codingfactory.restaurant.controllers to javafx.fxml;
    opens com.codingfactory.restaurant.models to javafx.fxml;
    opens com.codingfactory.restaurant.interfaces to javafx.fxml;

    exports com.codingfactory.restaurant;
    exports com.codingfactory.restaurant.controllers;
    exports com.codingfactory.restaurant.interfaces;
    exports com.codingfactory.restaurant.models;
}