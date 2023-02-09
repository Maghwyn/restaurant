package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.interfaces.ControllerInterface;
import com.codingfactory.restaurant.models.Dish;
import com.codingfactory.restaurant.singletons.DishHolder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;


public class MoreDishInfosController implements Initializable, ControllerInterface<DishesController> {
    private Dish dish;


    private DishesController dishesController;
    private Text text;
    private DishHolder holder;
    private Dish dishItem;

    @FXML
    private VBox vboxContainer;

    @FXML
    private Text category;

    @FXML
    private Text nameDish;

    @FXML
    private Text description;

    @FXML
    private Text price;

    @FXML
    private ImageView img;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initInstance();
        displayItemInfos();

    }

    public void initInstance() {
        holder = DishHolder.getInstance();
        dishItem = holder.getDish();
    }

    public void displayItemInfos() {
        category.setText(dishItem.getCategory());
        nameDish.setText(dishItem.getName());
        description.setText(dishItem.getDescription());
        price.setText(String.valueOf(dishItem.getPrice()) + "€");
        img.setImage(new Image(dishItem.getUrl()));
    }

    @Override
    public void setParentController(DishesController controller) {
        this.dishesController = controller;
    }
}
