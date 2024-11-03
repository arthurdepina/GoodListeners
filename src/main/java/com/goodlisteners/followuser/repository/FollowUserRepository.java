package com.goodlisteners.followuser.repository;

import com.goodlisteners.followuser.model.UserFollowers;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowUserRepository extends JpaRepository<UserFollowers, Integer> {
    boolean existsByUserIdAndFollowerId(Integer userId, Integer followerId);

    @Modifying
    @Transactional
    @Query("DELETE FROM UserFollowers uf WHERE uf.userId = :userId AND uf.followerId = :followerId")
    void deleteByUserIdAndFollowerId(@Param("userId") Integer userId, @Param("followerId") Integer followerId);
}
