package com.codingfactory.restaurant.models;

import org.bson.types.ObjectId;

public class Table {
    private ObjectId id;
    private int zone;
    private String number;
    private boolean status;
    private int nbrChairs;
    private int nbrTaken;

    public Table(ObjectId id, int zone, String number, boolean status, int nbrChair, int nbrTaken){
        this.id = id;
        this.zone = zone;
        this.number = number;
        this.status=status;
        this.nbrChairs=nbrChair;
        this.nbrTaken=nbrTaken;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int places) {
        this.zone = places;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus (boolean status) {
        this.status = status;
    }

    public int getNbrChairs() {
        return nbrChairs;
    }

    public void setNbrChairs(int nbrChairs) {
        this.nbrChairs = nbrChairs;
    }

    public int getNbrTaken() {
        return nbrTaken;
    }

    public void setNbrTaken(int nbrTaken) {
        this.nbrTaken = nbrTaken;
    }
}