package com.goodlisteners.followuser.service;

import com.goodlisteners.followuser.model.UserFollowers;
import com.goodlisteners.followuser.repository.FollowUserRepository;
import jakarta.transaction.Transactional;
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
        if (!followUserRepository.existsByUserIdAndFollowerId(userId, followerId)) {
            UserFollowers userFollower = new UserFollowers();
            userFollower.setUserId(userId);
            userFollower.setFollowerId(followerId);
            followUserRepository.save(userFollower);
        }
    }

    @Transactional
    public void unfollowUser(Integer userId, Integer followerId) {
        followUserRepository.deleteByUserIdAndFollowerId(userId, followerId);
    }
}
