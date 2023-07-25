package com.outingtracker.app.friendshipservicemain.model;

import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "friendships")
public class Friendship {

    @Id
    private String id;

    @NonNull
    private String inviterUserId;
    private String inviteeUserId = null;
    private String dummyUserId = null;
    private String status = null;

    public Friendship(){

    }

    public Friendship(String id, @NonNull String inviterUserId, String inviteeUserId, String dummyUserId, String status) {
        this.id = id;
        this.inviterUserId = inviterUserId;
        this.inviteeUserId = inviteeUserId;
        this.dummyUserId = dummyUserId;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NonNull
    public String getInviterUserId() {
        return inviterUserId;
    }

    public void setInviterUserId(@NonNull String inviterUserId) {
        this.inviterUserId = inviterUserId;
    }

    public String getInviteeUserId() {
        return inviteeUserId;
    }

    public void setInviteeUserId(String inviteeUserId) {
        this.inviteeUserId = inviteeUserId;
    }

    public String getDummyUserId() {
        return dummyUserId;
    }

    public void setDummyUserId(String dummyUserId) {
        this.dummyUserId = dummyUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
