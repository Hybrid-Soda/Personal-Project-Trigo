package com.mono.trigo.domain.plan.repository;

import com.mono.trigo.domain.plan.entity.Plan;
import org.springframework.data.repository.CrudRepository;

// 엔티티에 대해 기본적인 CRUD 기능 사용 (Plan Entity 와 기본키 Long 사용)
public interface PlanRepository extends CrudRepository<Plan, Long> {
}
