package com.outingtracker.app.friendshipservicemain.services;

import com.outingtracker.app.friendshipservicemain.model.DummyUser;
import com.outingtracker.app.friendshipservicemain.model.FriendshipModel;
import com.outingtracker.app.friendshipservicemain.model.User;
import com.outingtracker.app.friendshipservicemain.repository.DummyUserRepository;
import com.outingtracker.app.friendshipservicemain.repository.FriendshipRepository;
import com.outingtracker.app.friendshipservicemain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.outingtracker.app.friendshipservicemain.dto.FriendshipDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    private final DummyUserRepository dummyUserRepository;

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository, DummyUserRepository dummyUserRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.dummyUserRepository = dummyUserRepository;
    }

    private FriendshipDTO convertToDTO(FriendshipModel friendship) {
        User inviterUser = userRepository.findById(friendship.getInviterUserId()).orElse(null);
        User inviteeUser = friendship.getInviteeUserId() != null ? userRepository.findById(friendship.getInviteeUserId()).orElse(null) : null;
        DummyUser dummyUser = friendship.getDummyUserId() != null ? dummyUserRepository.findById(friendship.getDummyUserId()).orElse(null) : null;

        if (inviterUser == null) {
            throw new IllegalArgumentException("Invalid inviterUser ID found in friendship.");
        }

        return new FriendshipDTO(friendship.getId(), inviterUser, inviteeUser, dummyUser, friendship.getStatus());
    }

    // Service to add test data to the database
    public FriendshipModel addTestData(FriendshipModel friendship) {
        return friendshipRepository.save(friendship);
    }

    public List<FriendshipDTO> getAllFriendsByUserId(String userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }

        List<FriendshipModel> friendships= friendshipRepository.findByInviterUserIdAndStatusOrStatusIsNull(userId, "accepted");
        List<FriendshipModel> friendships1 = friendshipRepository.findByInviteeUserIdAndStatus(userId, "accepted");
        friendships.addAll(friendships1);


        List<FriendshipDTO> friendshipDTOS = new ArrayList<>();

         for(FriendshipModel friendship : friendships){


             FriendshipDTO friendshipDTO = convertToDTO(friendship);
             friendshipDTOS.add(friendshipDTO);
         }

         return friendshipDTOS;
    }

    public List<FriendshipDTO> getAllFriendsRequests(String userId) {
        List<FriendshipModel> friendships= friendshipRepository.findByInviterUserIdAndStatus(userId, "invited");

        List<FriendshipDTO> friendshipDTOS = new ArrayList<>();

        for(FriendshipModel friendship : friendships){

            FriendshipDTO friendshipDTO = convertToDTO(friendship);
            friendshipDTOS.add(friendshipDTO);
        }

        return friendshipDTOS;
    }

    public List<FriendshipDTO> getAllFriendsInvitations(String userId) {
        List<FriendshipModel> friendships= friendshipRepository.findByInviteeUserIdAndStatus(userId, "invited");

        List<FriendshipDTO> friendshipDTOS = new ArrayList<>();

        for(FriendshipModel friendship : friendships){

            FriendshipDTO friendshipDTO = convertToDTO(friendship);
            friendshipDTOS.add(friendshipDTO);
        }

        return friendshipDTOS;
    }

    public User getUserByUsername(String input) {
        return userRepository.findByUsername(input);
    }

    public FriendshipModel sendFriendRequest(String inviterUserId, String inviteeUserId) {
        User inviterUser = userRepository.findById(inviterUserId).orElse(null);
        User inviteeUser = userRepository.findById(inviteeUserId).orElse(null);

        if (inviterUser == null || inviteeUser == null) {
            throw new IllegalArgumentException("Invalid user IDs provided.");
        }

        // Check whether the invitee was already invited or friend
        FriendshipModel existingFriendship = friendshipRepository.findByInviterUserIdAndInviteeUserId(inviterUserId, inviteeUserId);

        if(existingFriendship != null && (existingFriendship.getStatus().equals("accepted") || existingFriendship.getStatus().equals("invited"))){
            System.out.println("Friendship Already Exists!");
            return existingFriendship;
        }else {

            FriendshipModel friendship = new FriendshipModel();
            friendship.setInviterUserId(inviterUserId);
            friendship.setInviteeUserId(inviteeUserId);
            friendship.setStatus("invited");

            // save friendship in the database
            return friendshipRepository.save(friendship);
        }
    }

    // Service method to create dummyUser and add it as friend
    public FriendshipModel createDummyUserAndaddFriend(String inviterUserId, String dummyUserName){

        User inviterUser= userRepository.findById(inviterUserId).orElse(null);

        if(inviterUser == null){
            throw new IllegalArgumentException("Invalid User ID provided");
        }

        DummyUser dummyUser = new DummyUser();
        dummyUser.setName(dummyUserName);

        // Save the dummy User in the database
        DummyUser savedDummyUser = dummyUserRepository.save(dummyUser);

        // Create friendship between the inviterUser and dummyUser
        FriendshipModel friendship = new FriendshipModel();
        friendship.setInviterUserId(inviterUserId);
        friendship.setDummyUserId(savedDummyUser.getId());


        // saving the friendship in the database

        return friendshipRepository.save(friendship);

    }

    public FriendshipModel acceptFriendInvitation(String userId, String friendshipId){

        FriendshipModel friendship  = friendshipRepository.findById(friendshipId).orElse(null);

        if(friendship  == null || !friendship .getInviteeUserId().equals(userId)){
            throw new IllegalArgumentException("Invalid invitation Id or user Id");
        }

        // now updating status to be "accepted"
        friendship.setStatus("accepted");

        return friendshipRepository.save(friendship );
    }

    public void cancelInvitation(String friendshipId, String userId) {
        FriendshipModel friendship = friendshipRepository.findById(friendshipId).orElse(null);
        if (friendship != null && "invited".equals(friendship.getStatus())) {
            friendshipRepository.delete(friendship);
        } else {
            throw new RuntimeException("Friendship not found with ID: " + friendshipId + " or it is not in invited status.");
        }
    }

    public FriendshipModel revokeFriendship(String friendshipId, String userId) {
        FriendshipModel friendship = friendshipRepository.findById(friendshipId).orElse(null);
        if (friendship != null && "accepted".equals(friendship.getStatus())) {
            friendship.setStatus("revoked");
            return friendshipRepository.save(friendship);
        } else {
            throw new RuntimeException("Friendship not found with ID: " + friendshipId + " or it is not accepted.");
        }
    }


    public FriendshipModel sendInvite(String nameOrEmail, String currentUser){

        User user = null;

        // checking if nameOrEmail is Email
        if(isValidEmail(nameOrEmail)){
            user = userRepository.findByEmail(nameOrEmail);
        }

        if(user != null){

            // ...Check if the user is already a friend
            FriendshipModel existingFriendship = friendshipRepository.findByInviterUserIdAndInviteeUserId(currentUser, user.getId());
            if (existingFriendship != null) {
                // User is already a friend, return null to indicate no friend request is sent
                return null;
            }

            // User with given email is registered, send invite
            FriendshipModel friendshipModel = new FriendshipModel();
            friendshipModel.setInviterUserId(currentUser);
            friendshipModel.setInviteeUserId(user.getId());
            friendshipModel.setStatus("invited");
            return friendshipRepository.save(friendshipModel);
        }else{

            // User with given email is not registered
            DummyUser dummyUser = new DummyUser();
            dummyUser.setName(nameOrEmail);
            dummyUserRepository.save(dummyUser);

            // creating friendship between them
            FriendshipModel friendshipModel = new FriendshipModel();
            friendshipModel.setInviterUserId(currentUser);
            friendshipModel.setDummyUserId(dummyUser.getId());
            return friendshipRepository.save(friendshipModel);
        }

    }

    private boolean isValidEmail(String email){
        return email.contains("@");
    }
}
