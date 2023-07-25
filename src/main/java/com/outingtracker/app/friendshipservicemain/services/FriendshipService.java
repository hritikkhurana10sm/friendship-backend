package com.outingtracker.app.friendshipservicemain.services;

import com.outingtracker.app.friendshipservicemain.model.DummyUser;
import com.outingtracker.app.friendshipservicemain.model.Friendship;
import com.outingtracker.app.friendshipservicemain.model.User;
import com.outingtracker.app.friendshipservicemain.repository.DummyUserRepository;
import com.outingtracker.app.friendshipservicemain.repository.FriendshipRepository;
import com.outingtracker.app.friendshipservicemain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.outingtracker.app.friendshipservicemain.dto.FriendshipDTO;

import java.util.ArrayList;
import java.util.Arrays;
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

    private FriendshipDTO convertToDTO(Friendship friendship) {
        User inviterUser = userRepository.findById(friendship.getInviterUserId()).orElse(null);
        User inviteeUser = friendship.getInviteeUserId() != null ? userRepository.findById(friendship.getInviteeUserId()).orElse(null) : null;
        DummyUser dummyUser = friendship.getDummyUserId() != null ? dummyUserRepository.findById(friendship.getDummyUserId()).orElse(null) : null;

        if (inviterUser == null) {
            throw new IllegalArgumentException("Invalid inviterUser ID found in friendship.");
        }

        return new FriendshipDTO(friendship.getId(), inviterUser, inviteeUser, dummyUser, friendship.getStatus());
    }

    // Service to add test data to the database
    public Friendship addTestData(Friendship friendship) {
        return friendshipRepository.save(friendship);
    }

    public List<FriendshipDTO> getAllFriendsByUserId(String userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null.");
        }

        List<Friendship> friendships= friendshipRepository.findByInviterUserIdAndStatusOrStatusIsNull(userId, "accepted");
        List<Friendship> friendships1 = friendshipRepository.findByInviteeUserIdAndStatus(userId, "accepted");
        friendships.addAll(friendships1);


        List<FriendshipDTO> friendshipDTOS = new ArrayList<>();

         for(Friendship friendship : friendships){


             FriendshipDTO friendshipDTO = convertToDTO(friendship);
             friendshipDTOS.add(friendshipDTO);
         }

         return friendshipDTOS;
    }

    public List<FriendshipDTO> getAllFriendsRequests(String userId) {
        List<Friendship> friendships= friendshipRepository.findByInviterUserIdAndStatus(userId, "invited");

        List<FriendshipDTO> friendshipDTOS = new ArrayList<>();

        for(Friendship friendship : friendships){

            FriendshipDTO friendshipDTO = convertToDTO(friendship);
            friendshipDTOS.add(friendshipDTO);
        }

        return friendshipDTOS;
    }

    public List<FriendshipDTO> getAllFriendsInvitations(String userId) {
        List<Friendship> friendships= friendshipRepository.findByInviteeUserIdAndStatus(userId, "invited");

        List<FriendshipDTO> friendshipDTOS = new ArrayList<>();

        for(Friendship friendship : friendships){

            FriendshipDTO friendshipDTO = convertToDTO(friendship);
            friendshipDTOS.add(friendshipDTO);
        }

        return friendshipDTOS;
    }

    public User getUserByUsername(String input) {
        return userRepository.findByUsername(input);
    }

    public Friendship sendFriendRequest(String inviterUserId, String inviteeUserId) {
        User inviterUser = userRepository.findById(inviterUserId).orElse(null);
        User inviteeUser = userRepository.findById(inviteeUserId).orElse(null);

        if (inviterUser == null || inviteeUser == null) {
            throw new IllegalArgumentException("Invalid user IDs provided.");
        }

        // Check whether the invitee was already invited or friend
        Friendship existingFriendship = friendshipRepository.findByInviterUserIdAndInviteeUserId(inviterUserId, inviteeUserId);

        if(existingFriendship != null && (existingFriendship.getStatus().equals("accepted") || existingFriendship.getStatus().equals("invited"))){
            System.out.println("Friendship Already Exists!");
            return existingFriendship;
        }else {

            Friendship friendship = new Friendship();
            friendship.setInviterUserId(inviterUserId);
            friendship.setInviteeUserId(inviteeUserId);
            friendship.setStatus("invited");

            // save friendship in the database
            return friendshipRepository.save(friendship);
        }
    }

    // Service method to create dummyUser and add it as friend
    public Friendship createDummyUserAndaddFriend(String inviterUserId, String dummyUserName){

        User inviterUser= userRepository.findById(inviterUserId).orElse(null);

        if(inviterUser == null){
            throw new IllegalArgumentException("Invalid User ID provided");
        }

        DummyUser dummyUser = new DummyUser();
        dummyUser.setName(dummyUserName);

        // Save the dummy User in the database
        DummyUser savedDummyUser = dummyUserRepository.save(dummyUser);

        // Create friendship between the inviterUser and dummyUser
        Friendship friendship = new Friendship();
        friendship.setInviterUserId(inviterUserId);
        friendship.setDummyUserId(savedDummyUser.getId());


        // saving the friendship in the database

        return friendshipRepository.save(friendship);

    }

    public Friendship acceptFriendInvitation(String userId, String inviteId){
//       System.out.println(userId);
//       System.out.println(inviteId);
        Friendship invitation = friendshipRepository.findById(inviteId).orElse(null);
//         System.out.println("hello");
//         System.out.println(invitation.getInviteeUserId().equals(userId));
        if(invitation == null || !invitation.getInviteeUserId().equals(userId)){
            throw new IllegalArgumentException("Invalid invitation Id or user Id");
        }

        // now updating status to be "accepted"
        invitation.setStatus("accepted");

        return friendshipRepository.save(invitation);
    }
}
