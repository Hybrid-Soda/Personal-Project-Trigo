package com.mono.trigo.domain.plan.repository;

import com.mono.trigo.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    List<Plan> findByUserId(Long userId);

    List<Plan> findAllByIsPublicTrue();
}
