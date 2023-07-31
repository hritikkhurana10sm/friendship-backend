package com.outingtracker.app.friendshipservicemain.services;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;


@Service
@RequestScope
public class GetUserServices {
    private String userId;

    @PostConstruct
    public void init() {}
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
