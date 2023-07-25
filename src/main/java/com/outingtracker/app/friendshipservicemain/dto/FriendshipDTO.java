package com.outingtracker.app.friendshipservicemain.dto;

import com.outingtracker.app.friendshipservicemain.model.DummyUser;
import com.outingtracker.app.friendshipservicemain.model.User;

public class FriendshipDTO {

    private String id;
    private User inviterUser;
    private User inviteeUser;
    private DummyUser dummyUser;
    private String status;

    public FriendshipDTO(String id, User inviterUser, User inviteeUser, DummyUser dummyUser, String status) {
        this.id = id;
        this.inviterUser = inviterUser;
        this.inviteeUser = inviteeUser;
        this.dummyUser = dummyUser;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getInviterUser() {
        return inviterUser;
    }

    public void setInviterUser(User inviterUser) {
        this.inviterUser = inviterUser;
    }

    public User getInviteeUser() {
        return inviteeUser;
    }

    public void setInviteeUser(User inviteeUser) {
        this.inviteeUser = inviteeUser;
    }

    public DummyUser getDummyUser() {
        return dummyUser;
    }

    public void setDummyUser(DummyUser dummyUser) {
        this.dummyUser = dummyUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
