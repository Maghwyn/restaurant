package com.codingfactory.restaurant.controllers;

import com.codingfactory.restaurant.MongoConnection;
import com.mongodb.client.MongoDatabase;


public class TableController {
    public static void main(String[] args) {
        MongoDatabase db = MongoConnection.getDatabase();
        db.getCollection("tables").find();

    }
}
