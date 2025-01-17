package com.mono.trigo.domain.content.repository;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.web.content.dto.ContentSearchCondition;

import java.util.List;

public interface ContentRepositoryCustom {

    List<Content> searchContents(ContentSearchCondition condition);
}
