package com.mono.trigo.domain.content.repository;

import com.mono.trigo.domain.content.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long>, ContentRepositoryCustom {
}
