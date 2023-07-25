package com.outingtracker.app.friendshipservicemain.controller;

import com.outingtracker.app.friendshipservicemain.dto.FriendshipDTO;
import com.outingtracker.app.friendshipservicemain.model.Friendship;
import com.outingtracker.app.friendshipservicemain.model.User;
import com.outingtracker.app.friendshipservicemain.repository.FriendshipRepository;
import com.outingtracker.app.friendshipservicemain.services.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/friend")
public class FriendshipController {

     @Autowired
     private FriendshipService friendshipService;

     // Adding test data to friendship
     @PostMapping("/test")
     public ResponseEntity<?> addTestData(@RequestBody Friendship friend){
         Friendship save = this.friendshipService.addTestData(friend);
         return ResponseEntity.ok(save);
     }

     // Get list of friends of the user TODO: Dummy user must be shown in friends list

     @GetMapping("/user/{userId}/friends")
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsByUserId(@PathVariable("userId") String userId){

          List<FriendshipDTO> friends = friendshipService.getAllFriendsByUserId(userId);
          return ResponseEntity.ok(friends);
     }

     // get list of requests user made
     @GetMapping("/user/{userId}/requests")
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsRequests(@PathVariable("userId") String userId){

          List<FriendshipDTO> friends = friendshipService.getAllFriendsRequests(userId);
          return ResponseEntity.ok(friends);
     }

     // Get list of invitations to user
     @GetMapping("/user/{userId}/invitations")
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsInvitations(@PathVariable("userId") String userId){

          List<FriendshipDTO> friends = friendshipService.getAllFriendsInvitations(userId);
          return ResponseEntity.ok(friends);
     }

     // Get users by usernameOrEmail
     @GetMapping("/user/search")
     public ResponseEntity<User> getUserByUsername(@RequestParam("input") String input){
          User user = friendshipService.getUserByUsername(input);
          return ResponseEntity.ok(user);
     }

     // Create Friendship TODO: SEND FRIEND REQUEST
     @PostMapping("/user/{inviterUserId}/invitee/{inviteeUserId}/add")
     public ResponseEntity<Friendship> sendFriendRequest(@PathVariable("inviterUserId") String inviterUserId , @PathVariable("inviteeUserId") String inviteeUserId){
          Friendship friendship = this.friendshipService.sendFriendRequest(inviterUserId , inviteeUserId);
          return ResponseEntity.ok(friendship);
     }

     @PostMapping("/user/{userId}/addDummyUser")
     public ResponseEntity<Friendship> createDummyUserAndaddFriend(@PathVariable("userId") String userId, @RequestParam("dummyUserName")String dummyUserName){

          Friendship friendship = friendshipService.createDummyUserAndaddFriend(userId , dummyUserName);
          return ResponseEntity.ok(friendship);
     }

     @PostMapping("/user/{userId}/inviteId/{inviteId}/acceptInvitation")
     public ResponseEntity<Friendship> acceptFriendInvitation(@PathVariable("userId") String userId, @PathVariable("inviteId") String inviteId){

          Friendship friendship = friendshipService.acceptFriendInvitation(userId, inviteId);
          return ResponseEntity.ok(friendship);
     }

}
