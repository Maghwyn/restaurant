package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Table;
import com.mongodb.client.MongoCollection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.bson.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class FormControllerNewMenu implements Initializable, ControllerInterface<TableController> {
    @FXML
    private TextField clientNumber;

    @FXML
    private Button bookTableBtn;

    @FXML
    private Text errorMessage;

    private TableController tableController;

    @Override
    public void setParentController(TableController controller) {
        this.tableController = controller;

        boolean canBook = tableController.factoryController.canTakeOrder;
        if(canBook == false) {
            bookTableBtn.setDisable(true);
            clientNumber.setDisable(true);
            errorMessage.setText("Impossible de prendre de nouvelles commandes, le service va bientôt se terminer");
            errorMessage.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookTableBtn.setOnMouseClicked(this::bookTable);
        clientNumber.setOnKeyReleased(this::verifyCapacity);
    }

    private MongoCollection commands() {
        return MongoConnection.getDatabase().getCollection("commands");
    }

    private void setPendingCommands(int cnb, String tid) {
        List<Document> docs = new ArrayList();

        // There's no other choice that i know of
        for(int n = 0; n < cnb; n++) {
            Document doc = new Document("tableId", tid)
                    .append("status", "PENDING")
                    .append("dishes", new ArrayList())
                    .append("total", 0)
                    .append("createdAt", new Date());
            docs.add(doc);
        }

        commands().insertMany(docs);
    }

    private void verifyCapacity(KeyEvent e) {
        String strCnb = clientNumber.getText();
        if(strCnb.isEmpty() || strCnb.isBlank()) return;

        Table currentTable = tableController.currentTable;
        int cnb = 999;

        try {
            cnb = Integer.parseInt(strCnb);
        } catch(Exception err) {
            errorMessage.setText("La valeur du champ n'est pas un nombre");
            errorMessage.setVisible(true);
            return;
        }

        if(cnb > currentTable.getNbrChairs()) {
            errorMessage.setText("Limite maximal de cette table dépassée");
            errorMessage.setVisible(true);
        } else {
            errorMessage.setVisible(false);
        }
    }

    private void bookTable(MouseEvent e) {
        String strCnb = clientNumber.getText();

        if(strCnb.isEmpty() || strCnb.isBlank()) {
            errorMessage.setText("Tous les champs sont requis");
            errorMessage.setVisible(true);
            return;
        }

        Table currentTable = tableController.currentTable;
        int cnb = 999;

        try {
            cnb = Integer.parseInt(strCnb);
        } catch(Exception err) {
            errorMessage.setText("La valeur du champ n'est pas un nombre");
            errorMessage.setVisible(true);
            return;
        }
        setPendingCommands(cnb, currentTable.getNumber());
        currentTable.setStatus(true);
        currentTable.setNbrTaken(cnb);

        tableController.editTable(currentTable);
        tableController.closeModal();
    }
}

