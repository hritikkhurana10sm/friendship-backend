package com.outingtracker.app.friendshipservicemain.controller;

import com.outingtracker.app.friendshipservicemain.dto.FriendshipDTO;
import com.outingtracker.app.friendshipservicemain.model.FriendshipModel;
import com.outingtracker.app.friendshipservicemain.model.User;
import com.outingtracker.app.friendshipservicemain.services.FriendshipService;
import com.outingtracker.app.friendshipservicemain.services.GetUserServices;
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
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsByUserId(){
          List<FriendshipDTO> friends = friendshipService.getAllFriendsByUserId();
          return ResponseEntity.ok(friends);
     }

     @GetMapping("/sent-requests")
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsRequests(){
          List<FriendshipDTO> friends = friendshipService.sentRequests();
          return ResponseEntity.ok(friends);
     }

     @GetMapping("/received-requests")
     public ResponseEntity<List<FriendshipDTO>> getAllFriendsInvitations(){
          List<FriendshipDTO> friends = friendshipService.receivedRequests();
          return ResponseEntity.ok(friends);
     }

     @GetMapping("/users/search")
     public ResponseEntity<User> getUserByUsername(@RequestParam("input") String input){
          User user = friendshipService.getUserByUsername(input);
          return ResponseEntity.ok(user);
     }

     @PostMapping("/{friendship_id}/accept")
     public ResponseEntity<FriendshipModel> acceptFriendInvitation(@PathVariable("friendship_id") String friendship_id){
          FriendshipModel friendship = friendshipService.acceptFriendInvitation( friendship_id);
          return ResponseEntity.ok(friendship);
     }

     @PostMapping("/{friendship_id}/cancel")
     public ResponseEntity<Void> cancelFriendInvitation(@PathVariable("friendship_id") String friendship_id ){
          friendshipService.cancelInvitation(friendship_id);
          return null;
     }

     @PostMapping("/{friendship_id}/revoke")
     public ResponseEntity<Void> revokeInvitation(@PathVariable("friendship_id") String friendship_id ){
          friendshipService.revokeFriendship(friendship_id);
          return null;
//          ResponseEntity.status(HttpStatus.OK).body("Friendship Revoked by User Successfully!")
     }

     @PostMapping("/invite")
     public ResponseEntity<FriendshipDTO> sendInvite(@RequestBody Map<String, String> inviteRequest) {
          String name = inviteRequest.get("name");
          String email = inviteRequest.get("email");
            System.out.println("heyyy" + name);
          System.out.println("heyyy" + email);
          if (StringUtils.hasText(name) || StringUtils.hasText(email)) {

               FriendshipDTO result = friendshipService.sendInvite(name, email);
               if (result != null) {

               return ResponseEntity.ok(result);
               }
               else{
                   return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
               }
          } else {
               // Both name and email are empty
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
         }

          }


     @GetMapping("/{friendship_id}/friend")
     public ResponseEntity<FriendshipDTO> getFriendshipById(@PathVariable("friendship_id") String friendship_id){
          FriendshipDTO friend = friendshipService.getFriendshipById(friendship_id);
          return ResponseEntity.ok(friend);
     }




}
