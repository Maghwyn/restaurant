package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Table;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class FormControllerNewTable implements Initializable, ControllerInterface<TableController> {

    @FXML
    private TextField tableZone;

    @FXML
    private TextField tableChair;

    @FXML
    private Button createTableBtn;

    @FXML
    private Text errorMessage;

    private TableController tableController;

    @Override
    public void setParentController(TableController controller) {
        this.tableController = controller;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTableBtn.setOnMouseClicked(this::createNewTable);
    }

    private void createNewTable(MouseEvent e) {
        String strZone = tableZone.getText();
        String strNbrChair = tableChair.getText();

        if(strZone.isEmpty() || strNbrChair.isEmpty()) {
            errorMessage.setText("All fields are required");
            errorMessage.setVisible(true);
            return;
        }

        boolean status = false;
        int zone = Integer.parseInt(strZone);
        String identifier = zone == 0 ? "T" : zone == 1 ? "H" : "BH";
        String number = identifier + (tableController.getTablesCount(zone) + 1);
        int nbrChair = Integer.parseInt(strNbrChair);
        int nbrTaken = 0;

        Table table = new Table(null, zone, number, status, nbrChair, nbrTaken);

        tableController.createNewTable(table);
        tableController.closeModal();
    }
}
