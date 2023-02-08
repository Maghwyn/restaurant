package com.codingfactory.restaurant.models;
import com.codingfactory.restaurant.MongoConnection;
import com.mongodb.client.MongoCollection;
import org.bson.types.ObjectId;

public class Dish {
    private ObjectId id;
    private String name;

    public Dish(String name, String description, int price, String url, int cost, String category, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.url = url;
        this.cost = cost;
        this.category = category;
        this.quantity = quantity;
    }

    private String description;
    private int price;
    private String url;
    private int cost;
    private String category;
    private int quantity;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public MongoCollection getCollection() {
        return MongoConnection.getDatabase().getCollection("dish");
    }
}
