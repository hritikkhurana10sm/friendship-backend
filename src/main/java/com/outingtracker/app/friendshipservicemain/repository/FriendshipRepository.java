package com.outingtracker.app.friendshipservicemain.repository;

import com.outingtracker.app.friendshipservicemain.model.FriendshipModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends MongoRepository<FriendshipModel, String> {

    List<FriendshipModel> findByStatus(String status);

    List<FriendshipModel> findByInviterUserIdOrInviteeUserIdAndStatusIn(String inviterUserId, String inviteeUserId, List<String> statuses);


    List<FriendshipModel> findByInviterUserIdOrInviteeUserIdAndStatusOrStatusIsNull(String userId , String invitee, String status , String status1);
    List<FriendshipModel> findByInviterUserIdAndStatus(String userId, String status);

    List<FriendshipModel> findByInviteeUserIdAndStatus(String userId, String status);

    List<FriendshipModel> findByInviterUserIdAndStatusOrStatusIsNull(String userId, String status);

    FriendshipModel findByInviterUserIdAndInviteeUserId(String inviterUserId, String inviteeUserId);
}
