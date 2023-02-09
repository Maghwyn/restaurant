package com.codingfactory.restaurant.models;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class Command {
    private ObjectId id;

    public Command(ObjectId id, ObjectId tableId, String status, ArrayList dishes, int total, Date dateCommand, Date datePayed) {
        this.id = id;
        this.tableId = tableId;
        this.status = status;
        this.dishes = dishes;
        this.total = total;
        this.dateCommand = dateCommand;
        this.datePayed = datePayed;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getTableId() {
        return tableId;
    }

    public void setTableId(ObjectId tableId) {
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

    public Date getDateCommand() {
        return dateCommand;
    }

    public void setDateCommand(Date dateCommand) {
        this.dateCommand = dateCommand;
    }

    public Date getDatePayed() {
        return datePayed;
    }

    public void setDatePayed(Date datePayed) {
        this.datePayed = datePayed;
    }

    private ObjectId tableId;
    private String status;
    private ArrayList dishes;
    private int total;
    private Date dateCommand;
    private Date datePayed;



}
