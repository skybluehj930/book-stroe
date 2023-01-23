package com.lhj.bookstore.repository.custom;

import static com.lhj.bookstore.entity.QBookInfoEntity.bookInfoEntity;
import static com.lhj.bookstore.entity.QSupplyBookEntity.supplyBookEntity;
import static com.lhj.bookstore.entity.QSupplyEntity.supplyEntity;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.lhj.bookstore.dto.req.SearchSupBookReq;
import com.lhj.bookstore.dto.res.BookInfoRes;
import com.lhj.bookstore.dto.res.SupBookRes;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SupplyRepositoryCustomImpl implements SupplyRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<SupBookRes> searchSupplyBook(SearchSupBookReq searchSupBookReq, Pageable pageable) {
		List<SupBookRes> content = queryFactory
			.selectFrom(supplyEntity)
			.innerJoin(supplyEntity.supplyBookList, supplyBookEntity)
			.innerJoin(supplyBookEntity.bookInfo, bookInfoEntity)
			.where(
					containsTitle(searchSupBookReq.getTitle())
					, containsWriter(searchSupBookReq.getWriter())
					, eqType(searchSupBookReq.getType())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.transform(groupBy(supplyEntity.id)
				.list(Projections.fields(SupBookRes.class
					, supplyEntity.id.as("supId")
					, supplyEntity.supplyAt
					, list(Projections.fields(BookInfoRes.class
							, bookInfoEntity.id
							, bookInfoEntity.title
							, bookInfoEntity.type
							, bookInfoEntity.quantity
							, bookInfoEntity.supPrice
							, bookInfoEntity.writer
							, bookInfoEntity.createdAt
							, bookInfoEntity.fixPrice
							, bookInfoEntity.discount
					)).as("bookInfo")
			)));
			
		JPAQuery<Long> countQuery = queryFactory
			.select(supplyEntity.count())
			.from(supplyEntity)
			.innerJoin(supplyEntity.supplyBookList, supplyBookEntity)
			.innerJoin(supplyBookEntity.bookInfo, bookInfoEntity)
			.where(
					containsTitle(searchSupBookReq.getTitle())
					, containsWriter(searchSupBookReq.getWriter())
					, eqType(searchSupBookReq.getType())
			);
		
		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchFirst);
	}
	
	private BooleanExpression containsTitle(String title) {
		return !StringUtils.isEmpty(title) ? bookInfoEntity.title.containsIgnoreCase(title) : null;
	}
	
	private BooleanExpression containsWriter(String writer) {
		return !StringUtils.isEmpty(writer) ? bookInfoEntity.writer.containsIgnoreCase(writer) : null;
	}
	
	private BooleanExpression eqType(String type) {
		return !StringUtils.isEmpty(type) ? bookInfoEntity.type.eq(type) : null;
	}
}
