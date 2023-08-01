package com.outingtracker.app.friendshipservicemain.services;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;
@Service
@RequestScope
public class GetUserServices {
    private String userId;

    @PostConstruct
    public void init() {
        String queryString = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getQueryString();

        if (queryString != null) {
            String[] queryParams = queryString.split("&");
            for (String param : queryParams) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && "userId".equals(keyValue[0])) {
                    this.userId = keyValue[1];
                    break;
                }
            }
        }
        System.out.println("userId " + userId);
    }

    public String getUserId() {
        return userId;
    }
}