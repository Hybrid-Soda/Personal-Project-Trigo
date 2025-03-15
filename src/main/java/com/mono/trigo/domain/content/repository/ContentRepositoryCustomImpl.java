package com.mono.trigo.domain.content.repository;

import com.mono.trigo.domain.content.entity.Content;
import com.mono.trigo.web.content.dto.ContentSearchCondition;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.mono.trigo.domain.content.entity.QContent.content;
import static org.springframework.util.StringUtils.hasText;

public class ContentRepositoryCustomImpl implements ContentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ContentRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<Content> searchContents(ContentSearchCondition condition, Pageable pageable) {
        List<Content> contents = queryFactory
                .selectFrom(content)
                .where(
                        areaCodeEq(condition.getAreaCode()),
                        areaDetailCodeEq(condition.getAreaDetailCode()),
                        contentTypeCodeEq(condition.getContentTypeCode())
                )
                .orderBy(getOrderSpecifier(condition.getArrange()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> count = queryFactory
                .select(content.count())
                .from(content)
                .where(
                        areaCodeEq(condition.getAreaCode()),
                        areaDetailCodeEq(condition.getAreaDetailCode()),
                        contentTypeCodeEq(condition.getContentTypeCode())
                );

        return PageableExecutionUtils.getPage(contents, pageable, count::fetchCount);
    }

    private OrderSpecifier<?> getOrderSpecifier(String arrange) {
        if ("A".equals(arrange)) {
            return content.title.asc();
        } else if ("C".equals(arrange)) {
            return content.modifiedDate.desc();
        } else if ("D".equals(arrange)) {
            return content.createdDate.desc();
        } else {
            return content.title.asc(); // Default
        }
    }

    private BooleanExpression areaCodeEq(String areaCode) {
        return hasText(areaCode) ? content.areaDetail.area.code.eq(areaCode) : null;
    }

    private BooleanExpression areaDetailCodeEq(String areaDetailCode) {
        return hasText(areaDetailCode) ? content.areaDetail.code.eq(areaDetailCode) : null;
    }

    private BooleanExpression contentTypeCodeEq(String contentTypeCode) {
        return hasText(contentTypeCode) ? content.contentType.code.eq(contentTypeCode) : null;
    }
}
