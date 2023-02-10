package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Dish;
import com.codingfactory.restaurant.singletons.DishHolder;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Filters.eq;
import static java.lang.Integer.parseInt;

public class FormUpdateAdminDishController implements Initializable, ControllerInterface<DishesController> {

    private DishHolder holder;
    private Dish dishItem;

    private DishesController dishesController;

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
    private TextField dishQuantity;

    @FXML
    private Button updateDish;
    @FXML
    private Text errorMessage;

    @FXML
    private ChoiceBox dishCategory;

    @FXML
    private Button deleteDish;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dishCategory.getItems().addAll("entrées", "plats", "desserts");
        initInstance();
        setItemValues();
        updateDish.setOnMouseClicked(this::updateDish);
        deleteDish.setOnMouseClicked(this::deleteDishItem);

    }

    /**
     * Init singleTons instance of Dish to get the dish selected and active
     */
    public void initInstance() {
        holder = DishHolder.getInstance();
        dishItem = holder.getDish();
    }

    /**
     * Add items values to filled all the fields to update the dish we want to
     */
    public void setItemValues() {
        dishCategory.setValue(dishItem.getCategory());
        dishName.setText(dishItem.getName());
        dishDescription.setText(dishItem.getDescription());
        dishUrl.setText(dishItem.getUrl());
        dishCostPrice.setText(String.valueOf(dishItem.getCost()));
        dishQuantity.setText(String.valueOf(dishItem.getQuantity()));
        dishPrice.setText(String.valueOf(dishItem.getPrice()));
    }

    /**
     * Method to update the Dish, checks if all the fields are not empty
     * Update the dish into the DB
     * @param e {@link MouseEvent}
     */
    public void updateDish(MouseEvent e) {
        if (dishName.getText().equals("") || dishDescription.getText().equals("") || dishPrice.getText().equals("") || dishUrl.getText().equals("") || dishCostPrice.getText().equals("") || dishQuantity.getText().equals("") || dishCategory.getValue().equals(null)) {
            errorMessage.setText("Veuillez remplir tous les champs.");
        } else {
            MongoCollection coll = MongoConnection.getDatabase().getCollection("dish");

            Document query = new Document().append("_id", dishItem.getId());
            Bson updates = Updates.combine(
                    Updates.set("name", dishName.getText()),
                    Updates.set("description", dishDescription.getText()),
                    Updates.set("price", parseInt(dishPrice.getText())),
                    Updates.set("url", dishUrl.getText()),
                    Updates.set("cost", parseInt(dishCostPrice.getText())),
                    Updates.set("category", dishCategory.getValue()),
                    Updates.set("quantity", parseInt(dishQuantity.getText()))
            );
            try {
                coll.updateOne(query, updates);
                errorMessage.setText("Votre plat a bien été modifié !");
                dishesController.majOnAddDish();
                dishesController.closeFormAddDish();
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }

    }

    /**
     * Method to delete the dish we want to delete into the DB
     * @param e {@link MouseEvent}
     */
    public void deleteDishItem(MouseEvent e) {
        Bson query = eq("_id", dishItem.getId());
        try {
            MongoCollection coll = MongoConnection.getDatabase().getCollection("dish");
            coll.deleteOne(query);
            errorMessage.setText("Votre plat a bien été retiré !");
            dishesController.majOnAddDish();
            dishesController.closeFormAddDish();
        } catch (MongoException me) {
            System.err.println("Unable to delete due to an error: " + me);
        }
    }

    @Override
    public void setParentController(DishesController controller) {
        this.dishesController = controller;
    }
}
