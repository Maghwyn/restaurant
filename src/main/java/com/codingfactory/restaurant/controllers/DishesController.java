package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.codingfactory.restaurant.models.Dish;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.bson.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DishesController implements Initializable {
    public List<Dish> attend = new ArrayList<Dish>();
    public ObservableList<Dish> list;
    private String name;
    private String description;
    private int price;
    private String url;
    private int cost;
    private String category;
    private int quantity;
    private int index = 0;
    private int indexRow = 0;
    private VBox vBox;
    private HBox hBox;
    private ColumnConstraints column;
    private Button redirectButton;
    private GridPane grid;
    private Text textCategory;


    @FXML
    private VBox containerToAppendGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAllDishes();
        if (!attend.isEmpty()) {
            renderDishes();
        } else {
            hBox = new HBox();
            hBox.setAlignment(Pos.CENTER);
            redirectButton = new Button();
            redirectButton.setText("Ajouter un plat");
            containerToAppendGrid.getChildren().add(hBox);
            hBox.getChildren().add(redirectButton);
            redirectButton.setOnAction(e -> {
                // TODO DISPLAY FORM TO ADD DISH
            });
        }

    }

    public void createDishIfNone() {

    }

    public void renderDishes () {
        getDishesEntries();
        getDishesMain();
        getDishesDeserts();

    }
    public ObservableList getAllDishes() {
        int pos;
        MongoCollection coll = MongoConnection.getDatabase().getCollection("dish");
        MongoCursor<Document> cursor = MongoConnection.getDatabase().getCollection("dish").find().iterator();
        try {
            for (int i = 0; i < coll.count(); i++) {
                Document doc = cursor.next();
                name = doc.getString("name");
                description = doc.getString("description");
                price = doc.getInteger("price");
                url = doc.getString("url");
                cost = doc.getInteger("cost");
                category = doc.getString("category");
                quantity = doc.getInteger("quantity");

                attend.add(new Dish(name, description, price, url, cost, category, quantity));
            }
            list = FXCollections.observableArrayList(attend);
            return list;
        } finally {
            cursor.close();
        }
    }

    public void getDishesEntries() {
        grid = new GridPane();
        grid.setGridLinesVisible(true);
        containerToAppendGrid.getChildren().add(grid);

        Collection<Dish> dishListEntries;
        dishListEntries = attend.stream().map(n -> (Dish) n).filter((Dish n) -> n.getCategory().equals("entrées")).collect(Collectors.toList());
        dishListEntries.stream().forEach((Dish n) -> {
            if (index == 7) {
                index = 0;
                indexRow ++;
                vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                column = new ColumnConstraints();
                column.setPercentWidth(20);
                grid.getColumnConstraints().add(column);
                grid.add(vBox, index, indexRow);
                vBox.getChildren().add(new Text(n.getName()));
                vBox.getChildren().add(new Text(n.getDescription()));
                vBox.getChildren().add(new Text(String.valueOf(n.getPrice())));
                vBox.getChildren().add(new Text(n.getUrl())); // TODO CHANGE TO IMG
                vBox.getChildren().add(new Text(n.getCategory()));
                vBox.getChildren().add(new Text(String.valueOf(n.getCost())));
                vBox.getChildren().add(new Text(String.valueOf(n.getQuantity())));
                index ++;
                getSelectedItem(vBox, "entrées");
            } else {
                vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                column = new ColumnConstraints();
                column.setPercentWidth(20);
                grid.getColumnConstraints().add(column);
                grid.add(vBox, index, indexRow);
                vBox.getChildren().add(new Text(n.getName()));
                vBox.getChildren().add(new Text(n.getDescription()));
                vBox.getChildren().add(new Text(String.valueOf(n.getPrice())));
                vBox.getChildren().add(new Text(n.getUrl())); // TODO CHANGE TO IMG
                vBox.getChildren().add(new Text(n.getCategory()));
                vBox.getChildren().add(new Text(String.valueOf(n.getCost())));
                vBox.getChildren().add(new Text(String.valueOf(n.getQuantity())));
                index++;
                getSelectedItem(vBox, "entrées");
            }
        });
    }
    public void getDishesMain() {
        grid = new GridPane();
        grid.setGridLinesVisible(true);
        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        textCategory = new Text();
        textCategory.setText("Plats");

        containerToAppendGrid.getChildren().add(hBox);
        hBox.getChildren().add(textCategory);
        containerToAppendGrid.getChildren().add(grid);
        index = 0;


        Collection<Dish> dishListMain;
        dishListMain = attend.stream().map(n -> (Dish) n).filter((Dish n) -> n.getCategory().equals("plats")).collect(Collectors.toList());
        dishListMain.stream().forEach((Dish n) -> {
            if (index == 7) {
                index = 0;
                indexRow ++;
                vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                column = new ColumnConstraints();
                column.setPercentWidth(20);
                grid.getColumnConstraints().add(column);
                grid.add(vBox, index, indexRow);
                vBox.getChildren().add(new Text(n.getName()));
                vBox.getChildren().add(new Text(n.getDescription()));
                vBox.getChildren().add(new Text(String.valueOf(n.getPrice())));
                vBox.getChildren().add(new Text(n.getUrl())); // TODO CHANGE TO IMG
                vBox.getChildren().add(new Text(n.getCategory()));
                vBox.getChildren().add(new Text(String.valueOf(n.getCost())));
                vBox.getChildren().add(new Text(String.valueOf(n.getQuantity())));
                index ++;
                getSelectedItem(vBox, "plats");
            } else {
                vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                column = new ColumnConstraints();
                column.setPercentWidth(20);
                grid.getColumnConstraints().add(column);
                grid.add(vBox, index, indexRow);
                vBox.getChildren().add(new Text(n.getName()));
                vBox.getChildren().add(new Text(n.getDescription()));
                vBox.getChildren().add(new Text(String.valueOf(n.getPrice())));
                vBox.getChildren().add(new Text(n.getUrl())); // TODO CHANGE TO IMG
                vBox.getChildren().add(new Text(n.getCategory()));
                vBox.getChildren().add(new Text(String.valueOf(n.getCost())));
                vBox.getChildren().add(new Text(String.valueOf(n.getQuantity())));
                index++;
                getSelectedItem(vBox, "plats");
            }

        });
    }
    public void getDishesDeserts() {
        grid = new GridPane();
        grid.setGridLinesVisible(true);
        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        textCategory = new Text();
        textCategory.setText("Desserts");

        containerToAppendGrid.getChildren().add(hBox);
        hBox.getChildren().add(textCategory);
        containerToAppendGrid.getChildren().add(grid);
        index = 0;


        Collection<Dish> dishListMain;
        dishListMain = attend.stream().map(n -> (Dish) n).filter((Dish n) -> n.getCategory().equals("desserts")).collect(Collectors.toList());
        dishListMain.stream().forEach((Dish n) -> {
            if (index == 7) {
                index = 0;
                indexRow ++;
                vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                column = new ColumnConstraints();
                column.setPercentWidth(20);
                grid.getColumnConstraints().add(column);
                grid.add(vBox, index, indexRow);
                vBox.getChildren().add(new Text(n.getName()));
                vBox.getChildren().add(new Text(n.getDescription()));
                vBox.getChildren().add(new Text(String.valueOf(n.getPrice())));
                vBox.getChildren().add(new Text(n.getUrl())); // TODO CHANGE TO IMG
                vBox.getChildren().add(new Text(n.getCategory()));
                vBox.getChildren().add(new Text(String.valueOf(n.getCost())));
                vBox.getChildren().add(new Text(String.valueOf(n.getQuantity())));
                index ++;
                getSelectedItem(vBox, "desserts");
            } else {
                vBox = new VBox();
                vBox.setAlignment(Pos.CENTER);
                column = new ColumnConstraints();
                column.setPercentWidth(20);
                grid.getColumnConstraints().add(column);
                grid.add(vBox, index, indexRow);
                vBox.getChildren().add(new Text(n.getName()));
                vBox.getChildren().add(new Text(n.getDescription()));
                vBox.getChildren().add(new Text(String.valueOf(n.getPrice())));
                vBox.getChildren().add(new Text(n.getUrl())); // TODO CHANGE TO IMG
                vBox.getChildren().add(new Text(n.getCategory()));
                vBox.getChildren().add(new Text(String.valueOf(n.getCost())));
                vBox.getChildren().add(new Text(String.valueOf(n.getQuantity())));
                index++;
                getSelectedItem(vBox, "desserts");
            }
        });

    }
    public void getSelectedItem(VBox vBoxClicked, String category) {
        vBoxClicked.setOnMouseClicked(e -> {
            Collection<Dish> dishList;

            Node source = (Node) e.getSource();
            int rowIndex = GridPane.getRowIndex(source);
            int columnIndex = GridPane.getColumnIndex(source);
            if (rowIndex != 0) {
                columnIndex = GridPane.getColumnIndex(source) + 7 * rowIndex;
            }

            dishList = attend.stream().map(n -> (Dish) n).filter((Dish n) -> n.getCategory().equals(category)).collect(Collectors.toList());
            Dish item = (Dish) dishList.toArray()[columnIndex];
            // WE GOT THE ITEM ON CLICK ON LIST RENDERED ON CARTE
            System.out.println(item.getName());

        });
    }
}


