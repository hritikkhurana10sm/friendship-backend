package com.outingtracker.app.friendshipservicemain.services;

import com.outingtracker.app.friendshipservicemain.model.DummyUser;
import com.outingtracker.app.friendshipservicemain.model.FriendshipModel;
import com.outingtracker.app.friendshipservicemain.model.User;
import com.outingtracker.app.friendshipservicemain.repository.DummyUserRepository;
import com.outingtracker.app.friendshipservicemain.repository.FriendshipRepository;
import com.outingtracker.app.friendshipservicemain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import com.outingtracker.app.friendshipservicemain.dto.FriendshipDTO;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final DummyUserRepository dummyUserRepository;
    private final GetUserServices getUserServices;

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository, DummyUserRepository dummyUserRepository, GetUserServices getUserServices) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
        this.dummyUserRepository = dummyUserRepository;
        this.getUserServices = getUserServices;
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

    public FriendshipModel addTestData(FriendshipModel friendship) {
        return friendshipRepository.save(friendship);
    }

    public List<FriendshipDTO> getAllFriendsByUserId() {

        String userId = getUserServices.getUserId();

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

    public List<FriendshipDTO> sentRequests() {

        List<FriendshipModel> friendships= friendshipRepository.findByInviterUserIdAndStatus(getUserServices.getUserId(), "invited");

        List<FriendshipDTO> friendshipDTOS = new ArrayList<>();

        for(FriendshipModel friendship : friendships){

            FriendshipDTO friendshipDTO = convertToDTO(friendship);
            friendshipDTOS.add(friendshipDTO);
        }

        return friendshipDTOS;
    }

    public List<FriendshipDTO> receivedRequests() {

        List<FriendshipModel> friendships= friendshipRepository.findByInviteeUserIdAndStatus(getUserServices.getUserId(), "invited");

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

    public FriendshipModel acceptFriendInvitation(String friendshipId){
        FriendshipModel friendship  = friendshipRepository.findByIdAndInviteeUserIdAndStatus(friendshipId, getUserServices.getUserId(), "invited");
        if(friendship == null){
            throw new RuntimeException("Friendship not found with ID: " + friendshipId + " or it is not in invited status.");
        }
        friendship.setStatus("accepted");
        return friendshipRepository.save(friendship );
    }

    public void cancelInvitation(String friendshipId) {
        FriendshipModel friendship = friendshipRepository.findByIdAndInviterUserIdAndStatus(friendshipId, getUserServices.getUserId(), "invited");
        if (friendship != null) {
            friendshipRepository.delete(friendship);
        } else {
            throw new RuntimeException("Friendship not found with ID: " + friendshipId + " or it is not in invited status.");
        }
    }

    public void revokeFriendship(String friendshipId) {
        FriendshipModel friendship = friendshipRepository.findByIdAndStatus(friendshipId, "accepted");

        if (friendship != null && (getUserServices.getUserId().equals(friendship.getInviteeUserId())
                || getUserServices.getUserId().equals(friendship.getInviterUserId()))) {
            friendshipRepository.delete(friendship);
        } else {
            throw new RuntimeException("Friendship not found with ID: " + friendshipId + " or it is not accepted.");
        }
    }

    public FriendshipModel sendInvite(String name, String email){

        User user = null;

        // checking if nameOrEmail is Email
        if(email != null){
            user = userRepository.findByEmail(email);
        }

        if(user != null){

            FriendshipModel existingFriendship = friendshipRepository.findByInviteeUserIdAndInviterUserIdOrInviteeUserIdAndInviterUserId(
                    getUserServices.getUserId(), user.getId(), user.getId(), getUserServices.getUserId()
            );

            if (existingFriendship != null) {
                return null;
            }

            // User with given email is registered, send invite
            FriendshipModel friendshipModel = new FriendshipModel(getUserServices.getUserId(),user.getId(),"invited");
            return friendshipRepository.save(friendshipModel);
        }else{

            String var = name == null?email:name;
            DummyUser dummyUser = new DummyUser(var);
            dummyUserRepository.save(dummyUser);

            FriendshipModel friendshipModel = new FriendshipModel(getUserServices.getUserId(),dummyUser.getId());
            return friendshipRepository.save(friendshipModel);
        }

    }


}
