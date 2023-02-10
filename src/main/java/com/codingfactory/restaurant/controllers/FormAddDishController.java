package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import com.mongodb.client.MongoCollection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.bson.Document;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class FormAddDishController implements Initializable, ControllerInterface<DishesController> {
    @FXML
    private TextField dishName;
    @FXML
    private TextArea dishDescription;
    @FXML
    private TextField dishPrice;
    @FXML
    private TextField dishUrl;
    @FXML
    private TextField dishCostPrice;
    @FXML
    private ChoiceBox dishCategory;
    @FXML
    private TextField dishQuantity;

    @FXML
    private Button addDish;
    @FXML
    private Text errorMessage;

    private DishesController dishesController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /**
         * Add choiceBox enum choice
         */
        dishCategory.getItems().addAll("entrées", "plats", "desserts");


        addDish.setOnMouseClicked(this::addDish);
    }

    /**
     * Method to check if fields are not empty and send the new dish into MongoDB
     * @param e {@link MouseEvent}
     */
    public void addDish(MouseEvent e) {
        if (dishName.getText().equals("") || dishDescription.getText().equals("") || dishPrice.getText().equals("") || dishUrl.getText().equals("") || dishCostPrice.getText().equals("") || dishQuantity.getText().equals("") || dishCategory.getValue().equals(null)) {
            errorMessage.setText("Veuillez remplir tous les champs.");
        } else {
            MongoCollection coll = MongoConnection.getDatabase().getCollection("dish");


            Document doc = new Document("name", dishName.getText())
                    .append("description", dishDescription.getText())
                    .append("price", parseInt(dishPrice.getText()))
                    .append("url", dishUrl.getText())
                    .append("cost", parseInt(dishCostPrice.getText()))
                    .append("category", dishCategory.getValue())
                    .append("quantity", parseInt(dishQuantity.getText()));
            coll.insertOne(doc);
            errorMessage.setText("Votre plat a bien été ajouté !");
            dishesController.majOnAddDish();
            dishesController.closeFormAddDish();
        }
    }

    @Override
    public void setParentController(DishesController controller) {
        this.dishesController = controller;
    }
}
