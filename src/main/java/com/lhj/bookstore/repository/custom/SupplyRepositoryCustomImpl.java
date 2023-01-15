package com.lhj.bookstore.repository.custom;

import static com.lhj.bookstore.entity.QSupplyBookEntity.supplyBookEntity;
import static com.lhj.bookstore.entity.QSupplyEntity.supplyEntity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.lhj.bookstore.dto.SearchSupplyBookDto;
import com.lhj.bookstore.dto.SupplyBookDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SupplyRepositoryCustomImpl implements SupplyRepositoryCustom {

	private final JPAQueryFactory queryFactory;
	
	@Override
	public List<SupplyBookDto> getSupplyBooks(Long supId) {
		return queryFactory
				.select(Projections.fields(SupplyBookDto.class
					, supplyEntity.id.as("supId")
					, supplyEntity.supplyAt.as("supplyAt")
					, supplyBookEntity.bookInfo.id.as("bookId")
					, supplyBookEntity.bookInfo.title
					, supplyBookEntity.bookInfo.type
					, supplyBookEntity.bookInfo.supPrice
					, supplyBookEntity.bookInfo.writer
					, supplyBookEntity.bookInfo.createdAt
					, supplyBookEntity.bookInfo.fixPrice
					, supplyBookEntity.bookInfo.discount
			))
			.from(supplyEntity)
			.leftJoin(supplyEntity.supplyBookList, supplyBookEntity)
			.leftJoin(supplyBookEntity.bookInfo)
			.where(
					supplyEntity.id.eq(supId)
			)
			.fetch();
	}

	@Override
	public Page<SearchSupplyBookDto> searchSupplyBook(SearchSupplyBookDto searchSupplyBookDto, Pageable pageable) {
		List<SearchSupplyBookDto> content = queryFactory
			.select(Projections.fields(SearchSupplyBookDto.class
				, supplyBookEntity.id.as("supBookId")
				, supplyEntity.id.as("supId")
				, supplyEntity.supplyAt.as("supplyAt")
				, supplyBookEntity.bookInfo.title
				, supplyBookEntity.bookInfo.type
				, supplyBookEntity.bookInfo.writer
			))
			.from(supplyEntity)
			.leftJoin(supplyEntity.supplyBookList, supplyBookEntity)
			.leftJoin(supplyBookEntity.bookInfo)
			.where(
					containsTitle(searchSupplyBookDto.getTitle())
					, containsWriter(searchSupplyBookDto.getWriter())
					, eqType(searchSupplyBookDto.getType())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		
		JPAQuery<Long> countQuery = queryFactory
			.select(supplyEntity.count())
			.from(supplyEntity)
			.leftJoin(supplyEntity.supplyBookList, supplyBookEntity)
			.leftJoin(supplyBookEntity.bookInfo)
			.where(
					containsTitle(searchSupplyBookDto.getTitle())
					, containsWriter(searchSupplyBookDto.getWriter())
					, eqType(searchSupplyBookDto.getType())
			);
		
		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchFirst);
	}
	
	private BooleanExpression containsTitle(String title) {
		return !StringUtils.isEmpty(title) ? supplyBookEntity.bookInfo.title.containsIgnoreCase(title) : null;
	}
	
	private BooleanExpression containsWriter(String writer) {
		return !StringUtils.isEmpty(writer) ? supplyBookEntity.bookInfo.writer.containsIgnoreCase(writer) : null;
	}
	
	private BooleanExpression eqType(String type) {
		return !StringUtils.isEmpty(type) ? supplyBookEntity.bookInfo.type.eq(type) : null;
	}

}
