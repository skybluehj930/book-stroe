package com.lhj.bookstore.repository.custom;

import static com.lhj.bookstore.entity.QBookInfoEntity.bookInfoEntity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.lhj.bookstore.dto.SearchBookInfoDto;
import com.lhj.bookstore.entity.BookInfoEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookInfoRepositoryCustomImpl implements BookInfoRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<BookInfoEntity> searchBookInfo(SearchBookInfoDto SearchBookInfoDto, Pageable pageable) {
		List<BookInfoEntity> content = queryFactory
				.selectFrom(bookInfoEntity)
				.where(
						eqType(SearchBookInfoDto.getType())
						, containsKeyword(SearchBookInfoDto.getKeyword())
						)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
				
		JPAQuery<Long> countQuery = queryFactory
				.select(bookInfoEntity.count())
				.from(bookInfoEntity)
				.where(
						eqType(SearchBookInfoDto.getType())
						, containsKeyword(SearchBookInfoDto.getKeyword())
				);
		
	    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchFirst);
	}

	private BooleanExpression containsKeyword(String keyword) {
		return !StringUtils.isEmpty(keyword) ? 
				bookInfoEntity.title.containsIgnoreCase(keyword)
				.or(bookInfoEntity.writer.containsIgnoreCase(keyword)) : null;
	}
	
	private BooleanExpression eqType(String type) {
		return !StringUtils.isEmpty(type) ? bookInfoEntity.type.eq(type) : null;
	}
}
