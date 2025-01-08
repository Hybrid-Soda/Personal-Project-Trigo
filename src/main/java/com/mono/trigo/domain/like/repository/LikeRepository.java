package com.mono.trigo.domain.like.repository;

import com.mono.trigo.domain.like.entity.Like;
import com.mono.trigo.domain.plan.entity.Plan;
import com.mono.trigo.domain.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    void deleteByUserAndPlan(User user, Plan plan);
}
