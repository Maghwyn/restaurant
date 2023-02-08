package com.codingfactory.restaurant;

import com.codingfactory.restaurant.controllers.FactoryController;
import com.mongodb.client.MongoDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader layoutLoader = new FXMLLoader(App.class.getResource("views/layout.fxml"));
        Parent layout = layoutLoader.load();
        FactoryController factoryController = layoutLoader.getController();
        factoryController.setApp(this);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        Scene scene = new Scene(layout);

        // Set the stage to be the size of the screen
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        stage.setTitle("El Ristorante");
        stage.setScene(scene);
        stage.show();

        // database use Connection
        MongoDatabase db = MongoConnection.getDatabase();
    }

    public static void main(String[] args) {
        launch();
    }
}