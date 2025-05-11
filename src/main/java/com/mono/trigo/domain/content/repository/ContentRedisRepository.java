package com.mono.trigo.domain.content.repository;

import com.mono.trigo.web.content.dto.ContentResponse;
import org.springframework.data.repository.CrudRepository;

public interface ContentRedisRepository extends CrudRepository<ContentResponse, String> { }
