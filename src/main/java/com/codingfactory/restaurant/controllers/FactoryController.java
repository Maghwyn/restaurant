package com.codingfactory.restaurant.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerFactory implements Initializable {
    @FXML
    private GridPane view;

    @FXML
    private ControllerDrawer controllerDrawer;

    public void setIncludedFXML(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            view.getChildren().clear();
            view.add(root, 0, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controllerDrawer.setFactoryController(this);
    }
}
