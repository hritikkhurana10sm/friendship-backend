package com.outingtracker.app.friendshipservicemain.repository;

import com.outingtracker.app.friendshipservicemain.model.Friendship;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends MongoRepository<Friendship, String> {

    List<Friendship> findByStatus(String status);

    List<Friendship> findByInviterUserIdOrInviteeUserIdAndStatusIn(String inviterUserId, String inviteeUserId, List<String> statuses);


    List<Friendship> findByInviterUserIdOrInviteeUserIdAndStatusOrStatusIsNull(String userId ,String invitee, String status , String status1);
    List<Friendship> findByInviterUserIdAndStatus(String userId, String status);

    List<Friendship> findByInviteeUserIdAndStatus(String userId, String status);

    List<Friendship> findByInviterUserIdAndStatusOrStatusIsNull(String userId, String status);

    Friendship findByInviterUserIdAndInviteeUserId(String inviterUserId,String inviteeUserId);
}
