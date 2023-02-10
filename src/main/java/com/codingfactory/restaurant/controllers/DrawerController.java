package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.FactoryInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Enum Target define each navigation pages value that will be used to identify
 * the user action on click.
 */
enum Target {
    MENU,
    TABLES,
    COMMANDS,
    EMPLOYEES,
    STATS,
    REPORT,
}

/**
 * Enum State define the open action state of the drawer.
 */
enum State {
    OPEN,
    CLOSED,
}


/**
 * Class DrawerController is a controller that manage the content displayed when navigating on the application.
 * It sets a drawer navigation bar on the side of the application that is closable to make space.
 * It implements the Initializable and FactoryInterface interfaces.
 * FactoryInterface is used to set the layout parent controller.
 * Initializable is used to initialize the components of the dialog.
 */
public class DrawerController implements Initializable, FactoryInterface {
    @FXML
    private GridPane drawer;

    @FXML
    private StackPane drawerContainer;

    @FXML
    private Button drawerToggle;

    /**
     * All of the Hbox below are the navigation options.
     */
    @FXML
    private HBox menu;

    @FXML
    private HBox tables;

    @FXML
    private HBox commands;

    @FXML
    private HBox employees;

    @FXML
    private HBox stats;

    @FXML
    private HBox report;

    /**
     * The layout FactoryController that manage all others controllers.
     */
    private FactoryController factoryController;


    @Override
    /**
     * Method initialize, initializes the drawer component on load.
     * Set all the buttons with a custom property based of an enum to load the routes.
     * Set all the buttons with a router event to navigate on the application.
     * @param url URL to initialize the components.
     * @param resourceBundle ResourceBundle to initialize the components.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawerToggle.getProperties().put("state", State.OPEN);
        menu.getProperties().put("target", Target.MENU);
        tables.getProperties().put("target", Target.TABLES);
        commands.getProperties().put("target", Target.COMMANDS);
        employees.getProperties().put("target", Target.EMPLOYEES);
        stats.getProperties().put("target", Target.STATS);
        report.getProperties().put("target", Target.REPORT);

        drawerToggle.setOnMouseClicked(this::drawerAction);
        menu.setOnMouseClicked(this::routerGoto);
        tables.setOnMouseClicked(this::routerGoto);
        commands.setOnMouseClicked(this::routerGoto);
        employees.setOnMouseClicked(this::routerGoto);
        stats.setOnMouseClicked(this::routerGoto);
        report.setOnMouseClicked(this::routerGoto);
    }

    @Override
    /**
     * Set the factoryController to the router class, so we can have access to its public functions.
     * @param controller: the parent of the Class DrawerController.
     */
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }

    /**
     * Method drawerAction toggle the size of the drawer between open and semi-closed.
     * @param e MouseEvent that triggered the method.
     */
    private void drawerAction(MouseEvent e) {
        Button toggle = (Button) e.getSource();
        State state = (State) toggle.getProperties().get("state");

        if(state.equals(State.OPEN)) {
            toggle.getProperties().put("state", State.CLOSED);
            drawer.setMaxWidth(75);
            drawerContainer.setMaxWidth(75);
            drawerContainer.setMinWidth(75);
            return;
        }

        if(state.equals(State.CLOSED)) {
            toggle.getProperties().put("state", State.OPEN);
            drawer.setMaxWidth(250);
            drawerContainer.setMaxWidth(250);
            drawerContainer.setMinWidth(250);
        }
    }

    /**
     * Method routerGoto is effectively the router itself.
     * It retrieves the property target to identify which Fxml file to load.
     * @param e MouseEvent that triggered the method.
     */
    private void routerGoto(MouseEvent e) {
        HBox menuOption = (HBox) e.getSource();
        Target target = (Target) menuOption.getProperties().get("target");

        if(target.equals(Target.MENU)) {
            factoryController.setIncludedFXML("views/dishesLayout.fxml");
            return;
        }

        if(target.equals(Target.TABLES)) {
            factoryController.setIncludedFXML("views/tables.fxml");
            return;
        }

        if(target.equals(Target.COMMANDS)) {
            factoryController.setIncludedFXML("views/commandsLayout.fxml");
            return;
        }

        if(target.equals(Target.EMPLOYEES)) {
            factoryController.setIncludedFXML("views/employees.fxml");
            return;
        }

        if(target.equals(Target.STATS)) {
            factoryController.setIncludedFXML("views/stats.fxml");
        }

        if(target.equals(Target.REPORT)) {
            factoryController.setIncludedFXML("views/report.fxml");
        }
    }
}
