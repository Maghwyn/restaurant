package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import com.codingfactory.restaurant.models.Dish;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FormAddCommandController implements Initializable, FactoryInterface {

    private String name;
    private ObjectId id;
    private String description;
    private int price;
    private String url;
    private int cost;
    private String category;
    private int quantity;
    private FactoryController factoryController;

    @FXML
    private Text tableId;
    @FXML
    private Text numberPersons;
    @FXML
    private ChoiceBox statusChoice;
    @FXML
    private VBox listDishContainer;

    @FXML
    private TableView tableViewDishes;

    @FXML
    private TableColumn nameDishCol;

    @FXML
    private TableColumn descriptionDishCol;

    @FXML
    private TableColumn priceDishCol;


    public List<Dish> attend = new ArrayList<Dish>();
    public ObservableList<Dish> list;
    private ListView listDishRendered;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllDishes();
        statusChoice.getItems().addAll("commandée","en cours", "livrée");
        addDishesList();

    }

    public void addDishesList() {
        tableViewDishes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<Dish> dishListAppend;
        dishListAppend = attend.stream().map(n -> (Dish) n).filter((Dish n) -> n.getQuantity() > 0).collect(Collectors.toCollection(FXCollections::observableArrayList));
        tableViewDishes.setItems(dishListAppend);
    }



    public ObservableList getAllDishes() {
        int pos;
        MongoCollection coll = MongoConnection.getDatabase().getCollection("dish");
        MongoCursor<Document> cursor = MongoConnection.getDatabase().getCollection("dish").find().iterator();
        try {
            for (int i = 0; i < coll.count(); i++) {
                Document doc = cursor.next();
                id = doc.getObjectId("_id");
                name = doc.getString("name");
                description = doc.getString("description");
                price = doc.getInteger("price");
                url = doc.getString("url");
                cost = doc.getInteger("cost");
                category = doc.getString("category");
                quantity = doc.getInteger("quantity");

                attend.add(new Dish(id, name, description, price, url, cost, category, quantity));
            }
            list = FXCollections.observableArrayList(attend);
            return list;
        } finally {
            cursor.close();
        }
    }

    @Override
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }
}
