package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FactoryController implements Initializable {
    @FXML
    private VBox view;

    @FXML
    private DrawerController drawerController;

    private App app;

    public void setIncludedFXML(String fxmlFile) {
        try {
            FXMLLoader viewLoader = new FXMLLoader(app.getClass().getResource(fxmlFile));
            Parent root = viewLoader.load();
            view.getChildren().clear();
            view.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setApp(App app) {
        this.app = app;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawerController.setFactoryController(this);
    }
}
