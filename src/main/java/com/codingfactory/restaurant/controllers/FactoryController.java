package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.App;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class FactoryController implements Initializable {
    @FXML
    private VBox view;

    @FXML
    private DrawerController drawerController; // Injected

    @FXML
    private ModalController modalController; // Injected

    @FXML
    private FactoryInterface pageController; // Injected

    private App app;

    public void setApp(App app) {
        this.app = app;
    }

    public void setIncludedFXML(String fxmlFile)  {
        try {
            FXMLLoader viewLoader = new FXMLLoader(app.getClass().getResource(fxmlFile));
            Parent root = viewLoader.load();
            FactoryInterface controller = viewLoader.getController();

            if(controller != null) {
                setFactoryControllerToChild(controller);
            }

            view.getChildren().clear();
            view.getChildren().add(root);
            pageController = controller;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setFactoryControllerToChild(FactoryInterface controller) {
        Optional<Method> methodToFind = Arrays
                .stream(controller.getClass().getMethods())
                .filter(method -> "setFactoryController".equals(method.getName()))
                .findFirst();

        if(methodToFind.isPresent()) {
            controller.setFactoryController(this);
        }
    }

    public void openModal(String fxmlFile) {
        try {
            FXMLLoader viewLoader = new FXMLLoader(app.getClass().getResource(fxmlFile));
            Parent root = viewLoader.load();
            modalController.openModal(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(pageController != null) {
            setFactoryControllerToChild(pageController);
        }
        drawerController.setFactoryController(this);
    }
}
