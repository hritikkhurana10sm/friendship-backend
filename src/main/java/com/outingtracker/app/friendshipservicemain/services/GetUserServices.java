package com.outingtracker.app.friendshipservicemain.services;

import jakarta.annotation.PostConstruct;
import org.example.model.UserModel;
import org.example.util.PrincipalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

@Service
@RequestScope
public class GetUserServices {
    private String userId;

     @Autowired
     private PrincipalDetails principalDetails;


    @PostConstruct
    public void init() {
        System.out.println("Radha->"  + principalDetails.getPrincipalDetails().getId());
//      this.userId=  this.principalDetails.getPrincipalDetails().getId();
//        System.out.println("User Id from auth " + this.userId);
        this.userId = "64c7545383109e59664065e1";
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }
}