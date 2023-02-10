package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import com.codingfactory.restaurant.models.Command;
import com.codingfactory.restaurant.models.Dish;
import com.codingfactory.restaurant.models.Employee;
import javafx.scene.input.MouseEvent;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CommandsController implements Initializable, FactoryInterface {
    private ObjectId id;
    private String tableId;
    private String status;
    private Date createdAt;
    private List<Dish> dishesListDb;
    public List<Command> attend = new ArrayList<Command>();
    public ObservableList<Command> list;
    private int total;
    private FactoryController factoryController;

    public Command currentCommand;

    @FXML
    private TableView commandsTableView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllCommands();
        setAllCommands();

        commandsTableView.setRowFactory(tv -> {
            TableRow row = new TableRow();
            row.setOnMouseClicked(this::openCommandToEdit);
            return row;
        });

    }

    public void openCommandToEdit(MouseEvent e) {
        TableRow row = (TableRow) e.getSource();
        currentCommand = (Command) row.getItem();

        this.factoryController.openModal("views/formAddCommand.fxml", this);
    }


    public ObservableList getAllCommands() {
        int pos;
        MongoCollection coll = MongoConnection.getDatabase().getCollection("commands");
        MongoCursor<Document> cursor = MongoConnection.getDatabase().getCollection("commands").find().iterator();
        try {
            for (int i = 0; i < coll.count(); i++) {
                Document doc = cursor.next();
                id = doc.getObjectId("_id");
                tableId = doc.getString("tableId");
                status = doc.getString("status");
                dishesListDb = (List<Dish>) doc.get("dishes");
                total = doc.getInteger("total");
                createdAt = doc.getDate("createdAt");

                attend.add(new Command(id,  tableId, status, (ArrayList) dishesListDb,  total,   createdAt));
            }
            list = FXCollections.observableArrayList(attend);
            return list;
        } finally {
            cursor.close();
        }
    }

    public void setAllCommands() {
        commandsTableView.setItems(list);
    }

    @Override
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }
}
