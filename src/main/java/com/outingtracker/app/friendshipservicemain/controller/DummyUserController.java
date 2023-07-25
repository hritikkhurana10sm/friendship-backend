package com.outingtracker.app.friendshipservicemain.controller;

import com.outingtracker.app.friendshipservicemain.model.DummyUser;
import com.outingtracker.app.friendshipservicemain.model.Friendship;
import com.outingtracker.app.friendshipservicemain.services.DummyUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dummyUser")
public class DummyUserController {

    @Autowired
    private DummyUserServices dummyUserServices;

    @PostMapping("/test")
    public ResponseEntity<?> addTestData(@RequestBody DummyUser dummyUser){
        DummyUser save = this.dummyUserServices.addTestData(dummyUser);
        return ResponseEntity.ok(save);
    }
}
