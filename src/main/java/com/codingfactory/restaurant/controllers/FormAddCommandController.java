package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import com.codingfactory.restaurant.models.Command;
import com.codingfactory.restaurant.models.Dish;
import com.codingfactory.restaurant.models.Table;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class FormAddCommandController implements Initializable, ControllerInterface<CommandsController> {

    private String name;
    private ObjectId id;
    private String description;
    private int price;
    private String url;
    private int cost;
    private String category;
    private int quantity;
    private CommandsController commandController;

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

    @FXML
    private Button majCommand;


    public List<Dish> attend = new ArrayList<Dish>();
    public ObservableList<Dish> list;
    private ListView listDishRendered;
    private List<Dish> listDishesToDb = new ArrayList<Dish>();

    private Dish dishToAdd;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getAllDishes();
        statusChoice.getItems().addAll("PENDING","PREPARATION", "SERVI", "DONE");
        addDishesList();
        tableViewDishes.setRowFactory(tv -> {
            TableRow row = new TableRow();
            row.setOnMouseClicked(this::addDish);
            return row;
        });
        majCommand.setOnMouseClicked(this::updateCommand);
    }

    public void updateCommand(MouseEvent e) {
        Command currentCommand = commandController.currentCommand;
//        int totalPrice = listDishesToDb.stream().map(n -> n.getPrice()).reduce((integer, integer2);
        System.out.println(statusChoice.getValue());
        MongoCollection coll = MongoConnection.getDatabase().getCollection("commands");
        Document query = new Document().append("_id", currentCommand.getId());
        Bson updates = Updates.combine(
                Updates.set("status", currentCommand.getStatus()),
                Updates.set("dishes", listDishesToDb),
                Updates.set("total", 20)
        );
        try {
            coll.updateOne(query, updates);
//            dishesController.majOnAddDish();
//            dishesController.closeFormAddDish();
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
        }

    }

    public void addFields() {
        Command currentCommand = commandController.currentCommand;
        tableId.setText(currentCommand.getTableId());
        statusChoice.setValue(currentCommand.getStatus());
    }

    public void addDish(MouseEvent e) {
        TableRow row = (TableRow) e.getSource();
        dishToAdd = (Dish) row.getItem();

        if(row.getProperties().get("selected") == null) {
            row.getProperties().put("selected", false);
        }

        if(row.getProperties().get("selected").equals(true)) {
            row.getProperties().put("selected", false);
            Dish dish = (Dish) row.getProperties().get("dish");
            listDishesToDb.remove(dish);
            row.getStyleClass().clear();
        } else {
            row.getProperties().put("selected", true);
            Dish newDish = new Dish(dishToAdd.getId(), dishToAdd.getName(), dishToAdd.getDescription(), dishToAdd.getPrice(), dishToAdd.getUrl(), dishToAdd.getCost(),dishToAdd.getCategory(), dishToAdd.getQuantity());
            row.getProperties().put("dish", newDish);
            listDishesToDb.add(newDish);
            row.getStyleClass().add("selected");
        }

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
    public void setParentController(CommandsController controller) {
        this.commandController = controller;
        addFields();
    }
}
