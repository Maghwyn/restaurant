package com.codingfactory.restaurant.models;

import org.bson.types.ObjectId;

public class Employee {
    private ObjectId id;
    private String name;
    private String job;

    private Integer workedHours;

    private String status;

    public Employee(ObjectId id, String name, String job, Integer workedHours, String status) {
        this.id = id;
        this.name = name;
        this.job = job;
        this.status = status;
        this.workedHours = workedHours;
    }

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

    public String getJob() { return job; }

    public void setJob(String job) { this.job = job; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public Integer getWorkedHours() { return workedHours; }
    public void setWorkedHours(Integer workedHours) { this.workedHours = workedHours; }
}
