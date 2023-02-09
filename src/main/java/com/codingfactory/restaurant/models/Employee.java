package com.codingfactory.restaurant.models;

import org.bson.types.ObjectId;

public class Employee {
    private ObjectId id;
    private String name;
    private String job;
    private String status;

    public Employee(String name, String job, String status) {
        this.name = name;
        this.job = job;
        this.status = status;
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

    public String job() { return job; }

    public void setJob(String job) { this.job = job; }
}
