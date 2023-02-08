package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.FactoryInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable, FactoryInterface {
    @FXML
    private Button modalTest1;

    @FXML
    private Button modalTest2;

    private FactoryController factoryController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modalTest1.setOnMouseClicked(this::test1);
        modalTest2.setOnMouseClicked(this::test2);
    }

    public void test1(MouseEvent e) {
        factoryController.openModal("views/page2.fxml");
    }

    public void test2(MouseEvent e) {
        factoryController.openModal("views/page3.fxml");
    }

    @Override
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }
}
