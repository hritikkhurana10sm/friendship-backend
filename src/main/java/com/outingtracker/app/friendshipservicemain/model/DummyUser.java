package com.outingtracker.app.friendshipservicemain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "dummyUser")
public class DummyUser {
    @Id
    private String id;

    private String name;

    public DummyUser(){}

    public DummyUser(String name) {
        this.name = name;
    }

    public DummyUser(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
