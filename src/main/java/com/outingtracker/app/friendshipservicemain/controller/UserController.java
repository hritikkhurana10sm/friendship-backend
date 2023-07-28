package com.outingtracker.app.friendshipservicemain.controller;

import com.outingtracker.app.friendshipservicemain.model.User;
import com.outingtracker.app.friendshipservicemain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/test")
    public ResponseEntity<?> addTestData(@RequestBody User user){
        User save = this.userService.addTestData(user);
        return ResponseEntity.ok(save);
    }
}
