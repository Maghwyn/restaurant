package com.codingfactory.restaurant.models;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class Command {
    private ObjectId id;
    private Date createdAt;

    public Command(ObjectId id, String tableId, String status, ArrayList dishes, int total, Date createdAt) {
        this.id = id;
        this.tableId = tableId;
        this.status = status;
        this.dishes = dishes;
        this.total = total;
        this.createdAt = createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList getDishes() {
        return dishes;
    }

    public void setDishes(ArrayList dishes) {
        this.dishes = dishes;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    private String tableId;
    private String status;
    private ArrayList dishes;
    private int total;



}
