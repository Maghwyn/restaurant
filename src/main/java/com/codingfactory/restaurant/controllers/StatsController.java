package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import com.codingfactory.restaurant.models.Command;
import com.codingfactory.restaurant.models.Dish;
import com.codingfactory.restaurant.models.Employee;
import com.codingfactory.restaurant.models.Table;
import com.mongodb.Mongo;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

public class StatsController implements Initializable, FactoryInterface {
    @FXML
    private Label menuTotalLabel;

    @FXML
    private VBox lowestPlaceholder;

    @FXML
    private VBox highestPlaceholder;

    @FXML
    private Label waitingPriceLabel;

    @FXML
    private Label collectedPriceLabel;

    @FXML
    private TableView lastFiveClientView;

    private Dish lowestDish;
    private Dish highestDish;

    private int menuTotal = 0;
    private int waitingTotalPrice = 0;
    private int collectedTotalPrice = 0;

    public ObservableList<Command> commandsList;
    private FactoryController factoryController;

    private MongoCollection dishes() {
        return MongoConnection.getDatabase().getCollection("dish");
    }

    private MongoCollection commands() {
        return MongoConnection.getDatabase().getCollection("commands");
    }

    @Override
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getTotalMenuCost();
        getLowHighDishPrice();
        getWaitingTotalPrice();
        getLastFiveServedClient();

        menuTotalLabel.setText(menuTotal + " €");
        waitingPriceLabel.setText(waitingTotalPrice + " €");
        collectedPriceLabel.setText(collectedTotalPrice + " €");
        lastFiveClientView.setItems(commandsList);
        setLowHighDishPrice();
    }

    private void setLowHighDishPrice() {
        lowestPlaceholder.getChildren().clear();
        highestPlaceholder.getChildren().clear();

        Label name1 = new Label();
        Label cat1 = new Label();
        Label cost1 = new Label();
        Label qty1 = new Label();
        name1.setText(lowestDish.getName());
        cat1.setText("Catégorie: " + lowestDish.getCategory());
        cost1.setText("Coût: " + lowestDish.getCost() + " €");
        qty1.setText("Quantitée: " + lowestDish.getQuantity());
        lowestPlaceholder.getChildren().add(name1);
        lowestPlaceholder.getChildren().add(cat1);
        lowestPlaceholder.getChildren().add(cost1);
        lowestPlaceholder.getChildren().add(qty1);

        Label name2 = new Label();
        Label cat2 = new Label();
        Label cost2 = new Label();
        Label qty2 = new Label();
        name2.setText(highestDish.getName());
        cat2.setText("Catégorie: " + highestDish.getCategory());
        cost2.setText("Coût: " + highestDish.getCost() + " €");
        qty2.setText("Quantitée: " + highestDish.getQuantity());
        highestPlaceholder.getChildren().add(name2);
        highestPlaceholder.getChildren().add(cat2);
        highestPlaceholder.getChildren().add(cost2);
        highestPlaceholder.getChildren().add(qty2);
    }

    private void getLastFiveServedClient() {
        if(commandsList != null) {
            commandsList.removeAll();
        }

        List<Command> commands = new ArrayList();
        MongoCursor<Document> cursor = commands().find(new Document("status", "SERVED")).iterator();

        try {
            while (cursor.hasNext()) {
                Document command = cursor.next();
                ObjectId id = command.getObjectId("_id");
                String tableId = command.getString("tableId");
                int total = command.getInteger("total");
                Date createdAt = command.getDate("createdAt");
                commands.add(new Command(id, tableId, null, null, total, createdAt));
            }
            commands = commands.stream().limit(5).toList();
            commandsList = FXCollections.observableArrayList(commands);
        } finally {
            cursor.close();
        }
    }

    private void getWaitingTotalPrice() {
        waitingTotalPrice = 0;
        MongoCursor<Document> cursor = commands().find(new Document("status", new Document("$in", Arrays.asList("PREPARATION", "SERVED")))).iterator();
        try {
            while (cursor.hasNext()) {
                Document command = cursor.next();
                int total = command.getInteger("total");
                waitingTotalPrice += total;
            }
        } finally {
            cursor.close();
        }
    }

    private void getCollectedTotalPrice() {
        collectedTotalPrice = 0;
        MongoCursor<Document> cursor = commands().find(new Document("status", "DONE")).iterator();
        try {
            while (cursor.hasNext()) {
                Document command = cursor.next();
                int total = command.getInteger("total");
                collectedTotalPrice += total;
            }
        } finally {
            cursor.close();
        }
    }

    private void getTotalMenuCost() {
        menuTotal = 0;
        MongoCursor<Document> cursor = dishes().find().iterator();
        try {
            while (cursor.hasNext()) {
                Document dish = cursor.next();
                int price = dish.getInteger("price");
                menuTotal += price;
            }
        } finally {
            cursor.close();
        }
    }

    private void getLowHighDishPrice() {
        FindIterable<Document> lowestPrice = dishes().find().sort(new Document("price", 1)).limit(1);
        FindIterable<Document> highestPrice = dishes().find().sort(new Document("price", -1)).limit(1);

        MongoCursor<Document> lowestCursor = lowestPrice.iterator();
        MongoCursor<Document> highestCursor = highestPrice.iterator();

        if (lowestCursor.hasNext()) {
            Document lowestDoc = lowestCursor.next();
            ObjectId id = lowestDoc.getObjectId("_id");
            String name = lowestDoc.getString("name");
            String description = lowestDoc.getString("description");
            int price = lowestDoc.getInteger("price");
            int cost = lowestDoc.getInteger("cost");
            String category = lowestDoc.getString("category");
            int quantity = lowestDoc.getInteger("quantity");

            lowestDish = new Dish(id, name, description, price, null, cost, category, quantity);
        }

        if (highestCursor.hasNext()) {
            Document highestDoc = highestCursor.next();
            ObjectId id = highestDoc.getObjectId("_id");
            String name = highestDoc.getString("name");
            String description = highestDoc.getString("description");
            int price = highestDoc.getInteger("price");
            int cost = highestDoc.getInteger("cost");
            String category = highestDoc.getString("category");
            int quantity = highestDoc.getInteger("quantity");

            highestDish = new Dish(id, name, description, price, null, cost, category, quantity);
        }
    }
}
