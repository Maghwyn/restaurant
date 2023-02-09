package com.codingfactory.restaurant.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ModalController implements Initializable {
    @FXML
    private AnchorPane modal;

    @FXML
    private HBox modalBackground;

    @FXML
    private Button modalCloseButton;

    @FXML
    private HBox modalContent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modalCloseButton.setOnMouseClicked(this::closeModal);
    }

    private void closeModal(MouseEvent e) {
        modal.setVisible(false);
        modalContent.getChildren().clear();
    }

    public void forceClose() {
        modal.setVisible(false);
        modalContent.getChildren().clear();
    }

    public void openModal(Parent root) {
        modal.setVisible(true);
        modalContent.getChildren().add(root);
    }
}
