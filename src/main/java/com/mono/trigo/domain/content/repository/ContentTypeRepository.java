package com.mono.trigo.domain.content.repository;

import com.mono.trigo.domain.content.entity.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentTypeRepository extends JpaRepository<ContentType, Long> {

    ContentType findByCode(String code);
}
