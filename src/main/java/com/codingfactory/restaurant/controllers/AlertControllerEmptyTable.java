package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Table;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class AlertControllerEmptyTable implements Initializable, ControllerInterface<TableController> {
    @FXML
    private Button cancel;

    @FXML
    private Button delete;

    private TableController tableController;

    @Override
    public void setParentController(TableController controller) {
        this.tableController = controller;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cancel.setOnMouseClicked(this::cancelAction);
        delete.setOnMouseClicked(this::deleteAction);
    }

    private void cancelAction(MouseEvent e) {
        tableController.closeModal();
    }

    private void deleteAction(MouseEvent e) {
        Table currentTable = tableController.currentTable;
        currentTable.setNbrTaken(0);
        currentTable.setStatus(false);

        tableController.editTable(currentTable);
        tableController.closeModal();
    }
}
