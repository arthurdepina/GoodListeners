package com.goodlisteners.followuser.repository;

import com.goodlisteners.followuser.model.UserFollowers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowUserRepository extends JpaRepository<UserFollowers, Integer> {
    boolean existsByUserIdAndFollowerId(Integer userId, Integer followerId);

    void deleteByUserIdAndFollowerId(Integer userId, Integer followerId);
}
