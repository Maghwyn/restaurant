package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Command;
import com.codingfactory.restaurant.models.Dish;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ArrayList;
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

    /**
     * Method to update command in db
     * @param e {@link MouseEvent}
     */
    public void updateCommand(MouseEvent e) {
        Command currentCommand = commandController.currentCommand;
//        int totalPrice = listDishesToDb.stream().map(n -> n.getPrice()).reduce((integer, integer2);

        MongoCollection coll = MongoConnection.getDatabase().getCollection("commands");
        currentCommand.setStatus((String) statusChoice.getValue());
        currentCommand.setTotal(20);

        List<Document> dishes = new ArrayList<>();
        for (Dish dish : listDishesToDb) {
            dishes.add(new Document("name", dish.getName())
                    .append("description", dish.getDescription())
                    .append("price", dish.getPrice())
                    .append("url", dish.getUrl())
                    .append("cost", dish.getCost())
                    .append("category", dish.getCategory())
                    .append("quantity", dish.getQuantity()));
        }

        Document doc = new Document("dishes", dishes)
                .append("status", currentCommand.getStatus())
                .append("total", 20);

        coll.updateOne(new Document("_id", currentCommand.getId()), new Document("$set", doc));
    }

    /**
     * Method to set text fields rendered in the layout
     */
    public void addFields() {
        Command currentCommand = commandController.currentCommand;
        tableId.setText(currentCommand.getTableId());
        statusChoice.setValue(currentCommand.getStatus());
    }

    /**
     * Method to select a dish and add it into a list to update the list of dishes chosen in the DB
     * @param e {@link MouseEvent}
     */
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

    /**
     * Method to append dish list in the Table View
     */
    public void addDishesList() {
        tableViewDishes.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<Dish> dishListAppend;
        dishListAppend = attend.stream().map(n -> (Dish) n).filter((Dish n) -> n.getQuantity() > 0).collect(Collectors.toCollection(FXCollections::observableArrayList));
        tableViewDishes.setItems(dishListAppend);
    }


    /**
     * Method to get all the dish from the DB
     * @return list {@link List<Dish>}
     */
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
