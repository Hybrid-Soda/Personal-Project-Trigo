package com.mono.trigo.domain.plan.repository;

import com.mono.trigo.web.plan.dto.PlanResponse;
import org.springframework.data.repository.CrudRepository;

public interface PlanRedisRepository extends CrudRepository<PlanResponse, Long> { }
