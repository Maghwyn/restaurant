package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Table;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class AlertControllerEmptyTable is a controller class triggered by the TableController.
 * It represents an alert dialog with a delete action on a booking table (effectively restore its state).
 * It implements the Initializable and TableController interfaces.
 * ControllerInterface is used to set the parent controller the dialog belongs to.
 * Initializable is used to initialize the components of the dialog.
 */
public class AlertControllerEmptyTable implements Initializable, ControllerInterface<TableController> {
    @FXML
    private Button cancel;

    @FXML
    private Button delete;

    /**
     * The parent that trigger the Class AlertControllerEmptyTable.
     */
    private TableController tableController;

    @Override
    /**
     * Set the parentController to the dialog class, so we can have access to its public functions.
     * @param controller The parent of the Class AlertControllerEmptyTable.
     */
    public void setParentController(TableController controller) {
        this.tableController = controller;
    }


    @Override
    /**
     * Method initialize, initializes the alert component on load.
     * @param url URL to initialize the components.
     * @param resourceBundle ResourceBundle to initialize the components.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancel.setOnMouseClicked(this::cancelAction);
        delete.setOnMouseClicked(this::deleteAction);
    }

    /**
     * Method cancelAction closes the modal and remove its child (controller and fxml).
     * @param e MouseEvent that triggered the method.
     */
    private void cancelAction(MouseEvent e) {
        tableController.closeModal();
    }

    /**
     * Method deleteAction deletes (reset) the table to its original state and closes the modal.
     * @param e MouseEvent that triggered the method.
     */
    private void deleteAction(MouseEvent e) {
        Table currentTable = tableController.currentTable;
        currentTable.setNbrTaken(0);
        currentTable.setStatus(false);

        tableController.editTable(currentTable);
        tableController.closeModal();
    }
}
