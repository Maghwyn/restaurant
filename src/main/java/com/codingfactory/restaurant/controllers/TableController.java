package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import com.codingfactory.restaurant.models.Table;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class TableController implements Initializable, FactoryInterface {

    @FXML
    private Button sortFreeBtn;

    @FXML
    private Button sortOccupiedBtn;

    @FXML
    private Button sortTerraceBtn;

    @FXML
    private Button sortHallBtn;

    @FXML
    private Button sortBackHallBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private Button createTableBtn;

    @FXML
    private GridPane gridTableContainer;

    private FactoryController factoryController;

    public ArrayList<Table> attend = new ArrayList();
    public ObservableList<Table> tablesList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTableBtn.setOnMouseClicked(this::openCreateTableModal);
        getTables();
        fillGrid();
    }

    @Override
    public void setFactoryController(FactoryController controller) {
        this.factoryController = controller;
    }

    private MongoCollection tables() {
        return MongoConnection.getDatabase().getCollection("tables");
    }

    private void openCreateTableModal(MouseEvent e) {
        factoryController.openModal("views/formNewTable.fxml", this);
    }

    public void closeModal() {
        factoryController.forceCloseModal();
    }

    private VBox setGridFactory(Table table) {
        boolean status = table.getStatus();
        String number = table.getNumber();
        int taken = table.getNbrTaken();
        int max = table.getNbrChairs();

        String displayState = status == false ? "Libre" : "Occupée";
        String styleClass = status == false ? "free" : "occupied";

        VBox vbox = new VBox();
        vbox.setSpacing(20.0);
        vbox.getStyleClass().setAll(styleClass);
        vbox.setAlignment(Pos.CENTER);

        Label name = new Label();
        name.setText("Table " + number);

        Label state = new Label();
        state.setText(displayState);

        Label disponibility = new Label();
        disponibility.setText(taken + "/" + max);

        vbox.getChildren().add(name);
        vbox.getChildren().add(state);
        vbox.getChildren().add(disponibility);
        return vbox;
    }

    private void fillGrid() {
        if(tablesList.size() == 0) return;

        AtomicInteger idx = new AtomicInteger();
        tablesList.stream()
                .forEachOrdered(table -> {
                    Integer index = idx.getAndIncrement();
                    int row = index / 4;
                    int col = index % 4;
                    gridTableContainer.add(setGridFactory(table), col, row);
                });
    }

    public void createNewTable(Table table) {
        ObjectId id = new ObjectId();

        Document newTable = new Document("number", table.getNumber())
                .append("zone", table.getZone())
                .append("status", table.getStatus())
                .append("nbrChairs", table.getNbrChairs())
                .append("nbrTaken", table.getNbrTaken());
        newTable.put("_id", id);
        tables().insertOne(newTable);
        table.setId(id);
    }

    public long getTablesCount(int places) {
        long count = tables().countDocuments(new Document("places", places));
        return count;
    }

    public ObservableList<Table> getTables() {
        MongoCursor<Document> cursor = tables().find().iterator();
        try {
            while (cursor.hasNext()) {
                Document table = cursor.next();
                ObjectId id = table.getObjectId("_id");
                int places = table.getInteger("zone");
                String number = table.getString("number");
                Boolean status = table.getBoolean("status");
                int nbrChairs = table.getInteger("nbrChairs");
                int nbrTaken = table.getInteger("nbrTaken");
                attend.add(new Table(id, places, number, status, nbrChairs, nbrTaken));
            }
            tablesList = FXCollections.observableArrayList(attend);
            return tablesList;
        } finally {
            cursor.close();
        }
    }
}