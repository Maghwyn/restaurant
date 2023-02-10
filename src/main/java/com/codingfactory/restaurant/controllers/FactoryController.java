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


/**
 * Class FactoryController is a controller class that manage the application layout, it is the core
 * controller of all other controllers.
 * It is responsible for displaying dynamically the main controller defined throught the DrawerController.
 * It implements the Initializable interface.
 * Initializable is used to initialize the components of the dialog.
 */
public class FactoryController implements Initializable {
    @FXML
    private HBox view;

    @FXML
    /**
     * Define the drawerController, it may be annotated as unused or linked but it's actually injected
     * throught nested controllers.
     */
    private DrawerController drawerController; // Injected

    @FXML
    /**
     * Define the modalController, it may be annotated as unused or linked but it's actually injected
     * throught nested controllers.
     */
    private ModalController modalController; // Injected

    @FXML
    /**
     * Define the timerController, it may be annotated as unused or linked but it's actually injected
     * throught nested controllers.
     */
    private TimerController timerController; // Injected

    @FXML
    /**
     * Define the pageController, the dynamic main child of the FactoryController,
     * it may be annotated as unused or linked but it's actually injected throught nested controllers.
     */
    private FactoryInterface pageController; // Injected

    /**
     * Instance of the Application.
     */
    private App app;

    /**
     * Boolean flag that defines if the admin can still take new order.
     */
    public boolean canTakeOrder = true;

    /**
     * Method setApp is used for the sole purpose of retrieving the instance of the app
     * inside the FactoryController.
     * @param app
     */
    public void setApp(App app) {
        this.app = app;
    }

    /**
     * Method setIncludedFXML will load the fxml file and append the FactoryController within it.
     * Its main role is to permit a router with the DrawerController.
     * @param fxmlFile String path of an fxml file.
     */
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

    /**
     * Method setFactoryControllerToChild will try to find the method "setFactoryController".
     * This method is defined if the child implement the FactoryInterface, effectively
     * transfering to the child a way to acess the FactoryController.
     * @param controller The FactoryController.
     */
    private void setFactoryControllerToChild(FactoryInterface controller) {
        Optional<Method> methodToFind = Arrays
                .stream(controller.getClass().getMethods())
                .filter(method -> "setFactoryController".equals(method.getName()))
                .findFirst();

        if(methodToFind.isPresent()) {
            controller.setFactoryController(this);
        }
    }

    /**
     * Method openModal is used by a child of the FactoryController when the child
     * request for the modal to open with a specific form.
     * The modal should have access to the data of that child for data manipulation
     * reason, and it will if the ControllerInterface<ControllerType> is defined.
     * @param fxmlFile Fxml file path
     * @param controller The child controller of FactoryController
     * @param <T> The type of the child controller, aka <ControllerType>
     */
    public <T> void openModal(String fxmlFile, T controller) {
        try {
            FXMLLoader viewLoader = new FXMLLoader(app.getClass().getResource(fxmlFile));
            Parent root = viewLoader.load();
            ControllerInterface childController = viewLoader.getController();

            Optional<Method> methodToFind = Arrays
                    .stream(childController.getClass().getMethods())
                    .filter(method -> "setParentController".equals(method.getName()))
                    .findFirst();

            if(methodToFind.isPresent()) {
                childController.setParentController(controller);
            }

            modalController.openModal(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method forceCloseModal it explanatory by itself.
     */
    public void forceCloseModal() {
        modalController.forceClose();
    }

    @Override
    /**
     * Method initialize, initializes the FactoryController on load.
     * @param url URL to initialize the components.
     * @param resourceBundle ResourceBundle to initialize the components.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(pageController != null) {
            setFactoryControllerToChild(pageController);
        }

        canTakeOrder = true;
        timerController.setFactoryController(this);
        drawerController.setFactoryController(this);
    }
}
