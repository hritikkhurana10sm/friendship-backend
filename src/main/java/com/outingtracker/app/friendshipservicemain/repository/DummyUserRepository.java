package com.outingtracker.app.friendshipservicemain.repository;

import com.outingtracker.app.friendshipservicemain.model.DummyUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DummyUserRepository extends MongoRepository<DummyUser, String> {
}
