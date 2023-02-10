package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.interfaces.FactoryInterface;
import com.codingfactory.restaurant.models.Table;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

enum TableStatus {
    FREE(false),
    OCCUPIED(true);

    private final boolean value;

    TableStatus(boolean b) {
        this.value = b;
    }

    public boolean getValue() {
        return value;
    }
}

enum TableZone {
    TERRACE(0),
    HALL(1),
    BACKHALL(2);

    private final int value;

    TableZone(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}

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
    private TextField searchInput;

    @FXML
    private Button createTableBtn;

    @FXML
    private GridPane gridTableContainer;

    public FactoryController factoryController;

    public ArrayList<Table> attend = new ArrayList();
    public ObservableList<Table> tablesList;

    public Table currentTable = null;

    private boolean tableStatus = TableStatus.FREE.getValue();
    private int tableZone = -1;

    private String tableNumber = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sortFreeBtn.getProperties().put("status", TableStatus.FREE.getValue());
        sortOccupiedBtn.getProperties().put("status", TableStatus.OCCUPIED.getValue());
        sortTerraceBtn.getProperties().put("zone", TableZone.TERRACE.getValue());
        sortHallBtn.getProperties().put("zone", TableZone.HALL.getValue());
        sortBackHallBtn.getProperties().put("zone", TableZone.BACKHALL.getValue());

        sortFreeBtn.setOnMouseClicked(this::setFilterStatus);
        sortOccupiedBtn.setOnMouseClicked(this::setFilterStatus);
        sortTerraceBtn.setOnMouseClicked(this::setFilterZone);
        sortHallBtn.setOnMouseClicked(this::setFilterZone);
        sortBackHallBtn.setOnMouseClicked(this::setFilterZone);
        searchBtn.setOnMouseClicked(this::setFilterSearch);
        createTableBtn.setOnMouseClicked(this::openCreateTableModal);

        getTablesFiltered();
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

    private void openEmptyTableModal(MouseEvent e) {
        VBox btn = (VBox) e.getSource();
        currentTable = (Table) btn.getProperties().get("table");

        factoryController.openModal("views/alertEmptyTable.fxml", this);
    }

    private void openNewMenuModal(MouseEvent e) {
        VBox btn = (VBox) e.getSource();
        currentTable = (Table) btn.getProperties().get("table");

        factoryController.openModal("views/formNewMenu.fxml", this);
    }

    public void closeModal() {
        currentTable = null;
        factoryController.forceCloseModal();
    }

    private void setFilterStatus(MouseEvent e) {
        Button btn = (Button) e.getSource();
        boolean status = (boolean) btn.getProperties().get("status");

        if(tableStatus != status) {
            tableStatus = status;
            getTablesFiltered();
            fillGrid();
        }
    }

    private void setFilterZone(MouseEvent e) {
        Button btn = (Button) e.getSource();
        int zone = (int) btn.getProperties().get("zone");

        if(tableZone != zone) {
            tableZone = zone;
        } else tableZone = -1;
        getTablesFiltered();
        fillGrid();
    }

    private void setFilterSearch(MouseEvent e) {
        String input = searchInput.getText();
        if(input.isEmpty() || input.isBlank()) return;

        tableNumber = input;
        getTablesFiltered();
        fillGrid();
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

        vbox.getProperties().put("table", table);
        if(status == true) {
            vbox.setOnMouseClicked(this::openEmptyTableModal);
        } else vbox.setOnMouseClicked(this::openNewMenuModal);

        return vbox;
    }

    private void fillGrid() {
        gridTableContainer.getChildren().clear();
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

        getTablesFiltered();
        fillGrid();
    }

    public void editTable(Table table) {
        Document editTable = new Document("number", table.getNumber())
                .append("zone", table.getZone())
                .append("status", table.getStatus())
                .append("nbrChairs", table.getNbrChairs())
                .append("nbrTaken", table.getNbrTaken());
        tables().updateOne(new Document("_id", table.getId()), new Document("$set", editTable));

        getTablesFiltered();
        fillGrid();
    }

    public long getTablesCount(int places) {
        long count = tables().countDocuments(new Document("places", places));
        return count;
    }

    public List<Document> getTablesOptions(int ncb) {
        MongoCursor<Document> cursor = tables().find(Filters.gte("nbrChairs", ncb)).iterator();
        List<Document> documents = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                documents.add(cursor.next());
            }
        } finally {
            cursor.close();
        }
        return documents;
    }

    public ObservableList<Table> getTablesFiltered() {
        if(tablesList != null) {
            attend = new ArrayList();
            tablesList.removeAll();
        }

        MongoCursor<Document> cursor = tables().find(new Document("status", tableStatus)).iterator();
        List<Document> documents = new ArrayList<>();
        try {
            while (cursor.hasNext()) {
                documents.add(cursor.next());
            }
        } finally {
            cursor.close();
        }

        Stream<Document> stream = documents.stream();

        if(tableZone > -1) {
            stream = stream.filter(d -> d.getInteger("zone").equals(tableZone));
        }

        if(tableNumber != null) {
            stream = stream.filter(d -> d.getString("number").equals(tableNumber));
        }

        attend.addAll(
                stream.map(table -> {
                    ObjectId id = table.getObjectId("_id");
                    int places = table.getInteger("zone");
                    String number = table.getString("number");
                    Boolean status = table.getBoolean("status");
                    int nbrChairs = table.getInteger("nbrChairs");
                    int nbrTaken = table.getInteger("nbrTaken");
                    return new Table(id, places, number, status, nbrChairs, nbrTaken);
                }).toList()
        );

        tablesList = FXCollections.observableArrayList(attend);
        return tablesList;
    }
}