package com.lhj.bookstore.repository.custom;

import static com.lhj.bookstore.entity.QBookInfoEntity.bookInfoEntity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.lhj.bookstore.dto.req.SearchBookInfoReq;
import com.lhj.bookstore.dto.res.BookInfoRes;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookInfoRepositoryCustomImpl implements BookInfoRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;

	@Override
	public BookInfoRes getBookInfo(Long bookId) {
		return queryFactory
				.select(Projections.fields(BookInfoRes.class
						, bookInfoEntity.id
						, bookInfoEntity.title
						, bookInfoEntity.type
						, bookInfoEntity.quantity
						, bookInfoEntity.supPrice
						, bookInfoEntity.writer
						, bookInfoEntity.createdAt
						, bookInfoEntity.fixPrice
						, bookInfoEntity.discount
				))
				.from(bookInfoEntity)
				.where(eqBookId(bookId))
				.fetchOne();
	}
	
	@Override
	public Page<BookInfoRes> searchBookInfo(SearchBookInfoReq SearchBookInfoReq, Pageable pageable) {
		List<BookInfoRes> content = queryFactory
				.select(Projections.fields(BookInfoRes.class
						, bookInfoEntity.id
						, bookInfoEntity.title
						, bookInfoEntity.type
						, bookInfoEntity.quantity
						, bookInfoEntity.supPrice
						, bookInfoEntity.writer
						, bookInfoEntity.createdAt
						, bookInfoEntity.fixPrice
						, bookInfoEntity.discount
						))
				.from(bookInfoEntity)
				.where(
						eqType(SearchBookInfoReq.getType())
						, containsKeyword(SearchBookInfoReq.getKeyword())
						)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
		
		JPAQuery<Long> countQuery = queryFactory
				.select(bookInfoEntity.count())
				.from(bookInfoEntity)
				.where(
						eqType(SearchBookInfoReq.getType())
						, containsKeyword(SearchBookInfoReq.getKeyword())
						);
		
		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchFirst);
	}
	
	
	private BooleanExpression eqBookId(Long bookId) {
		return bookInfoEntity.id.eq(bookId);
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
