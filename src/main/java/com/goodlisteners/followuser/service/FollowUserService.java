package com.goodlisteners.followuser.service;

import com.goodlisteners.followuser.repository.FollowUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowUserService {
    private final FollowUserRepository followUserRepository;

    @Autowired
    public FollowUserService(FollowUserRepository followUserRepository) {
        this.followUserRepository = followUserRepository;
    }

    public void followUser(Integer userId, Integer followerId) {
        followUserRepository.followUser(userId, followerId);
    }

    public void unfollowUser(Integer userId, Integer followerId) {
        followUserRepository.unfollowUser(userId, followerId);
    }
}