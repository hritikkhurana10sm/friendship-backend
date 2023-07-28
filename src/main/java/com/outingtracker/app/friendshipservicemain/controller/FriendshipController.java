package com.outingtracker.app.friendshipservicemain.controller;

import com.outingtracker.app.friendshipservicemain.dto.FriendshipDTO;
import com.outingtracker.app.friendshipservicemain.model.FriendshipModel;
import com.outingtracker.app.friendshipservicemain.model.User;
import com.outingtracker.app.friendshipservicemain.services.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/friendship")
public class FriendshipController {

     @Autowired
     private FriendshipService friendshipService;

     // Adding test data to friendship
     @PostMapping("/test")
     public ResponseEntity<?> addTestData(@RequestBody FriendshipModel friend){
         FriendshipModel save = this.friendshipService.addTestData(friend);
         return ResponseEntity.ok(save);
     }

     // Get list of friends of the user
     @GetMapping("/")
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsByUserId(){
          String userId = "64b665fc7c3f6721059ea018";
          List<FriendshipDTO> friends = friendshipService.getAllFriendsByUserId(userId);
          return ResponseEntity.ok(friends);
     }

     // get list of requests user made
     @GetMapping("/sent-requests")
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsRequests(){
          String userId = "64b665fc7c3f6721059ea018";
          List<FriendshipDTO> friends = friendshipService.getAllFriendsRequests(userId);
          return ResponseEntity.ok(friends);
     }

     // Get list of invitations to user
     @GetMapping("/received-requests")
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsInvitations(){
          String userId = "64b665fc7c3f6721059ea018";
          List<FriendshipDTO> friends = friendshipService.getAllFriendsInvitations(userId);
          return ResponseEntity.ok(friends);
     }

     // Get users by usernameOrEmail
     @GetMapping("/users/search")
     public ResponseEntity<User> getUserByUsername(@RequestParam("input") String input){
          User user = friendshipService.getUserByUsername(input);
          return ResponseEntity.ok(user);
     }

     @PostMapping("/{friendship_id}/accept")
     public ResponseEntity<FriendshipModel> acceptFriendInvitation(@PathVariable("friendship_id") String friendship_id){
          String userId = "64b665fc7c3f6721059ea018";
          FriendshipModel friendship = friendshipService.acceptFriendInvitation(userId, friendship_id);
          return ResponseEntity.ok(friendship);
     }

     @PostMapping("/{friendship_id}/cancel")
     public ResponseEntity<Void> cancelFriendInvitation(@PathVariable("friendship_id") String friendship_id){
          String userId = "64b665fc7c3f6721059ea018";
          friendshipService.cancelInvitation(friendship_id, userId);
          return null;
     }

     @PostMapping("/{friendship_id}/revoke")
     public ResponseEntity<FriendshipModel> revokeInvitation(@PathVariable("friendship_id") String friendship_id){
          String userId = "64b665fc7c3f6721059ea018";
          FriendshipModel friendship = friendshipService.revokeFriendship(friendship_id, userId);
          return ResponseEntity.ok(friendship);
     }

     @PostMapping("/invite")
     public ResponseEntity<FriendshipModel> sendInvite(@RequestBody Map<String, String> inviteRequest){
          String userId = "64b665fc7c3f6721059ea018";
          FriendshipModel result = friendshipService.sendInvite(inviteRequest.get("nameOrEmail"), userId);
          return ResponseEntity.ok(result);
     }


     // Create Friendship
//     @PostMapping("/user/{inviterUserId}/invitee/{inviteeUserId}/add")
//     public ResponseEntity<FriendshipModel> sendInvite(@PathVariable("inviterUserId") String inviterUserId , @PathVariable("inviteeUserId") String inviteeUserId){
//          FriendshipModel friendship = this.friendshipService.sendFriendRequest(inviterUserId , inviteeUserId);
//          return ResponseEntity.ok(friendship);
//     }

//     @PostMapping("/user/{userId}/create-friend")
//     public ResponseEntity<FriendshipModel> createDummyUserAndaddFriend(@PathVariable("userId") String userId, @RequestParam("dummyUserName")String dummyUserName){
//
//          FriendshipModel friendship = friendshipService.createDummyUserAndaddFriend(userId , dummyUserName);
//          return ResponseEntity.ok(friendship);
//     }
}
