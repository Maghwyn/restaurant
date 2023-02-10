package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class FormControllerNewTable implements Initializable, ControllerInterface<TableController> {

    @FXML
    private ComboBox tableZone;

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

        ObservableList<String> jobsList = FXCollections.observableArrayList();
        jobsList.add("En terrasse");
        jobsList.add("A l'entrée");
        jobsList.add("En arrière salle");

        tableZone.setItems(jobsList);
    }

    private void createNewTable(MouseEvent e) {
        int zone = tableZone.getSelectionModel().getSelectedIndex();
        String strNbrChair = tableChair.getText();

        if(strNbrChair.isEmpty() || zone == -1) {
            errorMessage.setText("Tous les champs sont requis");
            errorMessage.setVisible(true);
            return;
        }

        boolean status = false;
        String identifier = zone == 0 ? "T" : zone == 1 ? "H" : "BH";
        String number = identifier + (tableController.getTablesCount(zone) + 1);
        int nbrChair = Integer.parseInt(strNbrChair);
        int nbrTaken = 0;

        Table table = new Table(null, zone, number, status, nbrChair, nbrTaken);

        tableController.createNewTable(table);
        tableController.closeModal();
    }
}
