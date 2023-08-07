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
import java.util.NoSuchElementException;
import java.util.Optional;

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
        System.out.println("gey" + friendship.getInviterUserId());
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
       System.out.println(getUserServices.getUserId());
        List<FriendshipModel> friendships= friendshipRepository.findByInviteeUserIdAndStatus(getUserServices.getUserId(), "invited");
        System.out.println(getUserServices.getUserId());

        List<FriendshipDTO> friendshipDTOS = new ArrayList<>();
        System.out.println(getUserServices.getUserId());

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
        Optional<FriendshipModel> friendshipOptional = friendshipRepository.findById(friendshipId);

        if (friendshipOptional.isPresent()) {
            FriendshipModel friendship = friendshipOptional.get(); // Get the FriendshipModel from Optional
            if (getUserServices.getUserId().equals(friendship.getInviteeUserId())
                    || getUserServices.getUserId().equals(friendship.getInviterUserId())) {
                friendshipRepository.delete(friendship);
            } else {
                throw new RuntimeException("You are not authorized to revoke this friendship.");
            }
        } else {
            throw new NoSuchElementException("Friendship not found with ID: " + friendshipId);
        }
    }

    public FriendshipDTO sendInvite(String name, String email){
       String check = "64c7545383109e59664065e1";
        User user = null;

        // checking if nameOrEmail is Email
        if(email != null){
            user = userRepository.findByEmail(email);
        }

        if(user != null){

            FriendshipModel existingFriendship = friendshipRepository.findByInviteeUserIdAndInviterUserIdOrInviteeUserIdAndInviterUserId(
                    check, user.getId(), user.getId(), check
            );

            if (existingFriendship != null) {
                System.out.println("Friendship Not Found");
                return null;
            }

            // User with given email is registered, send invite
            FriendshipModel friendshipModel = new FriendshipModel(check,user.getId(),"invited");
            friendshipRepository.save(friendshipModel);
            FriendshipDTO result = convertToDTO(friendshipModel);

             return result;
        }else{

            String var = name.equals("")?email:name;
            DummyUser dummyUser = new DummyUser(var);
            dummyUserRepository.save(dummyUser);

            FriendshipModel friendshipModel = new FriendshipModel(check,dummyUser.getId());
            friendshipRepository.save(friendshipModel);
            FriendshipDTO result = convertToDTO(friendshipModel);

            return result;
        }

    }


    public FriendshipDTO getFriendshipById(String friendship_id) {
        String currentUserId = getUserServices.getUserId();

        Optional<FriendshipModel> friendshipOptional = friendshipRepository.findById(friendship_id);

        if (friendshipOptional.isPresent()) {
            FriendshipModel friendship = friendshipOptional.get();

            // Check if the current user is a part of this friendship
            if (currentUserId.equals(friendship.getInviterUserId()) || currentUserId.equals(friendship.getInviteeUserId())) {
                FriendshipDTO friendshipDTO = convertToDTO(friendship);
                return friendshipDTO;
            } else {
                throw new RuntimeException("You are not authorized to access this friendship.");
            }
        } else {
            throw new NoSuchElementException("Friendship not found with ID: " + friendship_id);
        }
    }



}
