package com.mono.trigo.domain.plan.repository;

import com.mono.trigo.domain.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
}
