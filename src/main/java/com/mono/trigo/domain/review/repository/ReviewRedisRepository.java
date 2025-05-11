package com.mono.trigo.domain.review.repository;

import com.mono.trigo.web.review.dto.ReviewsResponse;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRedisRepository extends CrudRepository<ReviewsResponse, Long> { }
