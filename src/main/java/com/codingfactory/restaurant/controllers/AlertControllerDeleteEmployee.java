package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.ControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class AlertControllerDeleteEmployee is a controller class triggered by the ModalController.
 * It represents an alert dialog with a delete action on an Employee.
 * It implements the Initializable and ControllerInterface interfaces.
 * ControllerInterface is used to set the parent controller the dialog belongs to.
 * Initializable is used to initialize the components of the dialog.
 */
public class AlertControllerDeleteEmployee implements Initializable, ControllerInterface<EmployeeController> {
    @FXML
    private CheckBox warningCheckbox;

    @FXML
    private Button cancel;

    @FXML
    private Button delete;

    /**
     * The parent that trigger the Class AlertControllerDeleteEmployee.
     */
    private EmployeeController employeeController;

    /**
     * Set the parentController to the dialog class, so we can have access to its public functions.
     * @param controller: the parent of the Class AlertControllerDeleteEmployee.
     */
    @Override
    public void setParentController(EmployeeController controller) {
        this.employeeController = controller;
    }


    /**
     * Method initialize, initializes the alert component on load.
     * @param url URL to initialize the components.
     * @param resourceBundle ResourceBundle to initialize the components.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warningCheckbox.setOnMouseClicked(this::toggleDeleteButton);
        cancel.setOnMouseClicked(this::cancelAction);
        delete.setOnMouseClicked(this::deleteAction);
    }

    /**
     * Method toggleDeleteButton, toggles the disabled state of delete button based on the checkbox selection.
     * @param e MouseEvent that triggered the method.
     */
    private void toggleDeleteButton(MouseEvent e) {
        if(warningCheckbox.isSelected()) {
            delete.setDisable(false);
        } else delete.setDisable(true);
    }

    /**
     * Method cancelAction closes the modal and remove its child (controller and fxml).
     * @param e MouseEvent that triggered the method.
     */
    private void cancelAction(MouseEvent e) {
        employeeController.closeModal();
    }

    /**
     * Method deleteAction deletes the employee and closes the modal.
     * @param e MouseEvent that triggered the method.
     */
    private void deleteAction(MouseEvent e) {
        employeeController.deleteEmployee();
        employeeController.closeModal();
    }
}
