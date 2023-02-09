package com.codingfactory.restaurant.models;

import org.bson.types.ObjectId;

public class Table {
    private ObjectId id;
    private int places;
    private int name;
    private boolean status;
    private int nbrChairs;
    private int nbrTaken;

    public Table(int places, int name, boolean status, int nbrChair, int nbrTaken){
        this.places = places;
        this.name = name;
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

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
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