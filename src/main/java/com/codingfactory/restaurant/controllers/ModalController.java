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

/**
 * Class ModalController is a controller class that manage a generic component modal accepting
 * any time of content that is added to the fxml HBox child.
 * It is responsible for displaying dynamically the content of the form and alert without
 * setting a defined height and width.
 * It implements the Initializable interface.
 * Initializable is used to initialize the components of the dialog.
 */
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
    /**
     * Method initialize, initializes the ModalController on load.
     * Define the closeButton event that will close the modal.
     * @param url URL to initialize the components.
     * @param resourceBundle ResourceBundle to initialize the components.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modalCloseButton.setOnMouseClicked(this::closeModal);
    }

    /**
     * Effectively close the modal and clear the children.
     * @param e MouseEvent that triggered the method.
     */
    private void closeModal(MouseEvent e) {
        modal.setVisible(false);
        modalContent.getChildren().clear();
    }

    /**
     * Effectively force close the modal and clear the children.
     */
    public void forceClose() {
        modal.setVisible(false);
        modalContent.getChildren().clear();
    }

    /**
     * Effectively open the modal and set the children.
     */
    public void openModal(Parent root) {
        modal.setVisible(true);
        modalContent.getChildren().add(root);
    }
}
