package com.outingtracker.app.friendshipservicemain.services;

import com.outingtracker.app.friendshipservicemain.model.DummyUser;
import com.outingtracker.app.friendshipservicemain.model.User;
import com.outingtracker.app.friendshipservicemain.repository.DummyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

@Service
public class DummyUserServices {

    private final DummyUserRepository dummyUserRepository;

    @Autowired
    public DummyUserServices(DummyUserRepository dummyUserRepository){
        this.dummyUserRepository = dummyUserRepository;
    }
    public DummyUser addTestData(DummyUser dummyUser){

        return dummyUserRepository.save(dummyUser);
    }

}
