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

/**
 * Class StatsController is a controller class with a reference to the layout FactoryController.
 * It is responsible for displaying the dashboard page defined in the DrawerController.
 * It implements the Initializable and FactoryInterface interfaces.
 * FactoryInterface is used to set the layout parent controller.
 * Initializable is used to initialize the components of the dialog.
 */
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

    /**
     * The layout FactoryController that manage all others controllers.
     */
    private FactoryController factoryController;

    /**
     * @return The mongodb collection dishes.
     */
    private MongoCollection dishes() {
        return MongoConnection.getDatabase().getCollection("dish");
    }

    /**
     * @return The mongodb collection commands.
     */
    private MongoCollection commands() {
        return MongoConnection.getDatabase().getCollection("commands");
    }

    @Override
    /**
     * The layout parentController, effectively the sceneController.
     */
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }

    @Override
    /**
     * Method initialize, initializes the stats page component on load.
     * It sets all the necessary data on the page.
     * @param url URL to initialize the components.
     * @param resourceBundle ResourceBundle to initialize the components.
     */
    public void initialize(URL location, ResourceBundle resources) {
        getTotalMenuCost();
        getLowHighDishPrice();
        getWaitingTotalPrice();
        getLastFiveServedClient();
        getCollectedTotalPrice();

        menuTotalLabel.setText(menuTotal + " €");
        waitingPriceLabel.setText(waitingTotalPrice + " €");
        collectedPriceLabel.setText(collectedTotalPrice + " €");
        lastFiveClientView.setItems(commandsList);
        setLowHighDishPrice();
    }

    /**
     * Method setLowHighDishPrice manually create the fxml informations
     * for the highest and lowest dishes prices.
     */
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

    /**
     * Method getLastFiveServedClient will find the commands document where the status is SERVED.
     * It set the commandsList with a limit of 5 documents to be displayed in the TableView.
     */
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

    /**
     * Method getWaitingTotalPrice will find the commands document where the status is SERVED and in PREPARATION.
     * Effectively meaning that the client has to pay at this point, so we can consider that they owe us money.
     */
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

    /**
     * Method getCollectedTotalPrice will find the commands document where the status is DONE.
     * That way we retrieve how much we gained from the clients so far.
     */
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

    /**
     * Method getTotalMenuCost retrieves all the dishes in the menu.
     * And set the menuTotal property with the total cost amount.
     */
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

    /**
     * Method getLowHighDishPrice determine the lowest and highest cost for a say dishes.
     * Set the lowestDish and highestDish with their specific dish.
     */
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
