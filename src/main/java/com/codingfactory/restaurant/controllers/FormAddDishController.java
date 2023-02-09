package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.mongodb.client.MongoCollection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.bson.Document;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class FormAddDishController implements Initializable {
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
    private TextField dishCategory;
    @FXML
    private TextField dishQuantity;

    @FXML
    private Button addDish;
    @FXML
    private Text errorMessage;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDish.setOnAction(event -> {
            if (dishName.getText().equals("") || dishDescription.getText().equals("") || dishPrice.getText().equals("") || dishUrl.getText().equals("") || dishCostPrice.getText().equals("") || dishCategory.getText().equals("") || dishQuantity.getText().equals("")) {
                errorMessage.setText("Veuillez remplir tous les champs.");
            } else {
                MongoCollection coll = MongoConnection.getDatabase().getCollection("dish");


                Document doc = new Document("name", dishName.getText())
                        .append("description", dishDescription.getText())
                        .append("price", parseInt(dishPrice.getText()))
                        .append("url", dishUrl.getText())
                        .append("cost", parseInt(dishCostPrice.getText()))
                        .append("category", dishCategory.getText())
                        .append("quantity", parseInt(dishQuantity.getText()));
                coll.insertOne(doc);
                errorMessage.setText("Votre plat a bien été ajouté !");
            }
        });
    }
}
