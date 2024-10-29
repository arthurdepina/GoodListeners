package com.goodlisteners.followuser.repository;

public interface FollowUserRepository {     // eu não sei o quão necessário é fazer uma interface para isso, maaaas...
    void followUser(Integer userId, Integer followerId);                       // o que importa é ser feliz (não sou).
    void unfollowUser(Integer userId, Integer followerId);
}