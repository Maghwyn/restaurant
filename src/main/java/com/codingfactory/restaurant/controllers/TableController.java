package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import com.codingfactory.restaurant.models.Table;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.bson.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TableController implements Initializable, FactoryInterface {

    public List<Table> tables = new ArrayList<Table>();
    public ObservableList<Table> listeTable;
    Integer zone;
    Integer name;
    Integer nbChairs;
    Boolean status;
    Integer nbTaken;
    private VBox vBox;
    private HBox hBox;
    private ColumnConstraints column;
    private GridPane grid;
    private Text textCategory;
    private FactoryController factoryController;

    @FXML
    HBox gridTableContainer;
    @FXML
    Text tableName;
    @FXML
    Button openFormAddTable;

    public static void main(String[] args) {

        MongoCollection coll = MongoConnection.getDatabase().getCollection("tables");
        MongoCursor<Document> cursor = MongoConnection.getDatabase().getCollection("tables").find().iterator();
        for (int i = 0; i < coll.count(); i++) {
            Document doc = cursor.next();
            System.out.println(doc);
        }
    }


    public void OpenFormAddTable() {
        openFormAddTable.setOnMouseClicked(e -> {
            factoryController.openModal("views/formNewTable.fxml", this);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }
}