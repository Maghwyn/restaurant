package com.codingfactory.restaurant.models;

import org.bson.types.ObjectId;

import java.util.Date;

public class Report {
    /**
     * Model to generate the financial situation of the restaurant
     */
    private ObjectId id;
    private Integer capital;
    private Integer expenditure;
    private Boolean isOngoing;
    private Date createdAt;

    public Report(ObjectId id, Integer capital, Integer expenditure, Date date, boolean isOngoing) {
        this.id = id;
        this.capital = capital;
        this.expenditure = expenditure;
        this.createdAt = date;
        this.isOngoing = isOngoing;
    }


    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Integer getCapital() {
        return capital;
    }

    public void setCapital(Integer capital) {
        this.capital = capital;
    }

    public Boolean getOngoing() {
        return isOngoing;
    }

    public void setOngoing(Boolean ongoing) {
        isOngoing = ongoing;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(Integer expenditure) {
        this.expenditure = expenditure;
    }
}
