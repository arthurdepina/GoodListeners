package com.goodlisteners.followuser.controller;

import com.goodlisteners.followuser.service.FollowUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/connections")
public class FollowUserController {
    private final FollowUserService followUserService;

    @Autowired
    public FollowUserController(FollowUserService followUserService) {
        this.followUserService = followUserService;
    }

    @PostMapping("/follow-user")
    public ResponseEntity<String> followUser(
            @RequestHeader(value = "user_id") Integer userId,
            @RequestHeader(value = "follower_id") Integer followerId
    ) {
        followUserService.followUser(userId, followerId);
        return ResponseEntity.ok("Usuário seguido com sucesso!");
    }

    @DeleteMapping("/unfollow-user")
    public ResponseEntity<String> unfollowUser(
            @RequestHeader(value = "user_id") Integer userId,
            @RequestHeader(value = "follower_id") Integer followerId) {
        followUserService.unfollowUser(userId, followerId);
        return ResponseEntity.ok("Usuário deixou de ser seguido com sucesso!");
    }
}
