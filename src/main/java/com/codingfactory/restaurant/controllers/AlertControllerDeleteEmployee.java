package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.ControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AlertControllerDeleteEmployee implements Initializable, ControllerInterface<EmployeeController> {
    @FXML
    private CheckBox warningCheckbox;

    @FXML
    private Button cancel;

    @FXML
    private Button delete;

    private EmployeeController employeeController;

    @Override
    public void setParentController(EmployeeController controller) {
        this.employeeController = controller;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warningCheckbox.setOnMouseClicked(this::toggleDeleteButton);
        cancel.setOnMouseClicked(this::cancelAction);
        delete.setOnMouseClicked(this::deleteAction);
    }

    private void toggleDeleteButton(MouseEvent e) {
        if(warningCheckbox.isSelected()) {
            delete.setDisable(false);
        } else delete.setDisable(true);
    }

    private void cancelAction(MouseEvent e) {
        employeeController.closeModal();
    }

    private void deleteAction(MouseEvent e) {
        employeeController.deleteEmployee();
        employeeController.closeModal();
    }
}
