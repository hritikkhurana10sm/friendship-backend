package com.outingtracker.app.friendshipservicemain.controller;

import com.outingtracker.app.friendshipservicemain.dto.FriendshipDTO;
import com.outingtracker.app.friendshipservicemain.model.FriendshipModel;
import com.outingtracker.app.friendshipservicemain.model.User;
import com.outingtracker.app.friendshipservicemain.services.FriendshipService;
import com.outingtracker.app.friendshipservicemain.services.GetUserServices;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/friendship")
public class FriendshipController {

     @Autowired
     private FriendshipService friendshipService;
     @Autowired
     private GetUserServices getUserServices;


     @PostMapping("/test")
     public ResponseEntity<?> addTestData(@RequestBody FriendshipModel friend){
         FriendshipModel save = this.friendshipService.addTestData(friend);
         return ResponseEntity.ok(save);
     }

     @GetMapping("/")
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsByUserId(@RequestParam("userId") String userId){
          getUserServices.setUserId(userId);
          List<FriendshipDTO> friends = friendshipService.getAllFriendsByUserId();
          return ResponseEntity.ok(friends);
     }

     @GetMapping("/sent-requests")
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsRequests(@RequestParam("userId") String userId){
          getUserServices.setUserId(userId);
          List<FriendshipDTO> friends = friendshipService.getAllFriendsRequests();
          return ResponseEntity.ok(friends);
     }

     @GetMapping("/received-requests")
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsInvitations(@RequestParam("userId") String userId){
          getUserServices.setUserId(userId);
          List<FriendshipDTO> friends = friendshipService.getAllFriendsInvitations();
          return ResponseEntity.ok(friends);
     }

     @GetMapping("/users/search")
     public ResponseEntity<User> getUserByUsername(@RequestParam("input") String input){
          User user = friendshipService.getUserByUsername(input);
          return ResponseEntity.ok(user);
     }

     @PostMapping("/{friendship_id}/accept")
     public ResponseEntity<FriendshipModel> acceptFriendInvitation(@PathVariable("friendship_id") String friendship_id , @RequestParam("userId") String userId){
          getUserServices.setUserId(userId);
          FriendshipModel friendship = friendshipService.acceptFriendInvitation( friendship_id);
          return ResponseEntity.ok(friendship);
     }

     @PostMapping("/{friendship_id}/cancel")
     public ResponseEntity<Void> cancelFriendInvitation(@PathVariable("friendship_id") String friendship_id , @RequestParam("userId") String userId){
          getUserServices.setUserId(userId);
          friendshipService.cancelInvitation(friendship_id);
          return null;
     }

     @PostMapping("/{friendship_id}/revoke")
     public ResponseEntity<String> revokeInvitation(@PathVariable("friendship_id") String friendship_id, @RequestParam("userId") String userId ){
          getUserServices.setUserId(userId);
          friendshipService.revokeFriendship(friendship_id);
          return ResponseEntity.status(HttpStatus.OK).body("Friendship Revoked by User Successfully!");
     }

     @PostMapping("/invite")
     public ResponseEntity<String> sendInvite(@RequestBody Map<String, String> inviteRequest, @RequestParam("userId") String userId){
          getUserServices.setUserId(userId);
          String name = inviteRequest.get("name");
          String email = inviteRequest.get("email");

          if(StringUtils.hasText(name) || StringUtils.hasText(email)){

               FriendshipModel result = friendshipService.sendInvite(name, email);

               if(result != null) {
                    return ResponseEntity.status(HttpStatus.OK).body("Friend Request sent successfully");
               }else{
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Already friends / Already Send-Requests or Received");
               }
          } else {
               // Both name and email are empty
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Provide atleast one of email or name");
          }

     }


}
