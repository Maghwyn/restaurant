package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.App;
import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class FactoryController implements Initializable {
    @FXML
    private HBox view;

    @FXML
    private DrawerController drawerController; // Injected

    @FXML
    private ModalController modalController; // Injected

    @FXML
    private TimerController timerController; // Injected

    @FXML
    private FactoryInterface pageController; // Injected

    private App app;

    public boolean canTakeOrder = true;

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
            view.setHgrow(root, Priority.ALWAYS);
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

    public <T> void openModal(String fxmlFile, T controller) {
        try {
            FXMLLoader viewLoader = new FXMLLoader(app.getClass().getResource(fxmlFile));
            Parent root = viewLoader.load();
            ControllerInterface childController = viewLoader.getController();

            Optional<Method> methodToFind = Arrays
                    .stream(childController.getClass().getMethods())
                    .filter(method -> "setParentController".equals(method.getName()))
                    .findFirst();

            System.out.println(methodToFind.isPresent());
            if(methodToFind.isPresent()) {
                childController.setParentController(controller);
            }

            modalController.openModal(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forceCloseModal() {
        modalController.forceClose();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(pageController != null) {
            setFactoryControllerToChild(pageController);
        }

        canTakeOrder = true;
        timerController.setFactoryController(this);
        drawerController.setFactoryController(this);
    }
}
