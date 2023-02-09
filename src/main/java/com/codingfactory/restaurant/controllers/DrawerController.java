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

enum Target {
    MENU,
    TABLES,
    COMMANDS,
    EMPLOYEES,
    REPORT,
}

enum State {
    OPEN,
    CLOSED,
}

public class DrawerController implements Initializable, FactoryInterface {
    @FXML
    private GridPane drawer;

    @FXML
    private StackPane drawerContainer;

    @FXML
    private Button drawerToggle;

    @FXML
    private HBox menu;

    @FXML
    private HBox tables;

    @FXML
    private HBox commands;

    @FXML
    private HBox employees;

    @FXML
    private HBox report;

    private FactoryController factoryController;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawerToggle.getProperties().put("state", State.OPEN);
        menu.getProperties().put("target", Target.MENU);
        tables.getProperties().put("target", Target.TABLES);
        commands.getProperties().put("target", Target.COMMANDS);
        employees.getProperties().put("target", Target.EMPLOYEES);
        report.getProperties().put("target", Target.REPORT);

        drawerToggle.setOnMouseClicked(this::drawerAction);
        menu.setOnMouseClicked(this::routerGoto);
        tables.setOnMouseClicked(this::routerGoto);
        commands.setOnMouseClicked(this::routerGoto);
        employees.setOnMouseClicked(this::routerGoto);
        report.setOnMouseClicked(this::routerGoto);
    }

    @Override
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }

    private void drawerAction(MouseEvent e) {
        Button toggle = (Button) e.getSource();
        State state = (State) toggle.getProperties().get("state");

        if(state.equals(State.OPEN)) {
            toggle.getProperties().put("state", State.CLOSED);
            drawer.setMaxWidth(75);
            drawerContainer.setMaxWidth(75);
            return;
        }

        if(state.equals(State.CLOSED)) {
            toggle.getProperties().put("state", State.OPEN);
            drawer.setMaxWidth(250);
            drawerContainer.setMaxWidth(250);
        }
    }

    private void routerGoto(MouseEvent e) {
        HBox menuOption = (HBox) e.getSource();
        Target target = (Target) menuOption.getProperties().get("target");

        if(target.equals(Target.MENU)) {
            factoryController.setIncludedFXML("views/page1.fxml");
            return;
        }

        if(target.equals(Target.TABLES)) {
            factoryController.setIncludedFXML("views/page2.fxml");
            return;
        }

        if(target.equals(Target.COMMANDS)) {
            factoryController.setIncludedFXML("views/page3.fxml");
            return;
        }

        if(target.equals(Target.EMPLOYEES)) {
            factoryController.setIncludedFXML("views/employees.fxml");
            return;
        }

        if(target.equals(Target.REPORT)) {
            factoryController.setIncludedFXML("views/page5.fxml");
        }
    }
}
