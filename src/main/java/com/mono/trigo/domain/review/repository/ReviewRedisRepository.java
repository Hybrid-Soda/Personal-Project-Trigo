package com.mono.trigo.domain.review.repository;

import com.mono.trigo.web.review.dto.ReviewListResponse;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRedisRepository extends CrudRepository<ReviewListResponse, String> { }
