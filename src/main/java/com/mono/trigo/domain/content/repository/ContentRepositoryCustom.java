package com.mono.trigo.domain.content.repository;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.web.content.dto.ContentSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ContentRepositoryCustom {

    Page<Content> searchContents(ContentSearchCondition condition, Pageable pageable);
}
