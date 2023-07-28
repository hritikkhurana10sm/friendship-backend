package com.outingtracker.app.friendshipservicemain.services;

import com.outingtracker.app.friendshipservicemain.model.User;
import com.outingtracker.app.friendshipservicemain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User addTestData(User user){
        return userRepository.save(user);
    }
}
