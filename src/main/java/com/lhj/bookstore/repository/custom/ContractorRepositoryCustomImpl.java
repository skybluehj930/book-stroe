package com.lhj.bookstore.repository.custom;

import static com.lhj.bookstore.entity.QBookInfoEntity.bookInfoEntity;
import static com.lhj.bookstore.entity.QContractorEntity.contractorEntity;
import static com.lhj.bookstore.entity.QSupplyBookEntity.supplyBookEntity;
import static com.lhj.bookstore.entity.QSupplyEntity.supplyEntity;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.ObjectUtils;

import com.lhj.bookstore.dto.req.SearchContSupBookReq;
import com.lhj.bookstore.dto.req.SearchContReq;
import com.lhj.bookstore.dto.res.BookInfoRes;
import com.lhj.bookstore.dto.res.ContSupBookRes;
import com.lhj.bookstore.dto.res.ContractorRes;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ContractorRepositoryCustomImpl implements ContractorRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;
	
	@Override
	public Page<ContractorRes> searchContractor(SearchContReq searchContReq, Pageable pageable) {
		List<ContractorRes> content = queryFactory
			.select(Projections.fields(ContractorRes.class
					, contractorEntity.id
					, contractorEntity.lowest
					, contractorEntity.stateCd
					, contractorEntity.contractAt
			))
			.from(contractorEntity)
			.where(
					eqStateCd(searchContReq.getStateCd())
					, betweenContractAt(searchContReq.getStartAt(), searchContReq.getEndAt())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
		
		JPAQuery<Long> countQuery = queryFactory
			.select(contractorEntity.count())
			.from(contractorEntity)
			.where(
					eqStateCd(searchContReq.getStateCd())
					, betweenContractAt(searchContReq.getStartAt(), searchContReq.getEndAt())
			);
		
		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchFirst);
	}
	
	@Override
	public Page<ContSupBookRes> searchContSupBook(SearchContSupBookReq searchContSupBookReq,
			Pageable pageable) {
		List<ContSupBookRes> content = queryFactory
			.selectFrom(contractorEntity)
			.innerJoin(contractorEntity.supplyList, supplyEntity)
			.innerJoin(supplyEntity.supplyBookList, supplyBookEntity)
			.innerJoin(supplyBookEntity.bookInfo, bookInfoEntity)
			.where(
					containsTitle(searchContSupBookReq.getTitle())
					, containsWriter(searchContSupBookReq.getWriter())
					, eqType(searchContSupBookReq.getType())
					, eqStateCd(searchContSupBookReq.getStateCd())
			)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.transform(groupBy(contractorEntity.id)
				.list(Projections.fields(ContSupBookRes.class
					, contractorEntity.id.as("contId")
					, contractorEntity.lowest
					, contractorEntity.stateCd
					, contractorEntity.contractAt
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
			.select(contractorEntity.count())
			.from(contractorEntity)
			.innerJoin(contractorEntity.supplyList, supplyEntity)
			.innerJoin(supplyEntity.supplyBookList, supplyBookEntity)
			.innerJoin(supplyBookEntity.bookInfo, bookInfoEntity)
			.where(
					containsTitle(searchContSupBookReq.getTitle())
					, containsWriter(searchContSupBookReq.getWriter())
					, eqType(searchContSupBookReq.getType())
					, eqStateCd(searchContSupBookReq.getStateCd())
			);
		
		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchFirst);
	}
	
	@Override
	public ContSupBookRes getContSupBook(Long contId) {
		List<ContSupBookRes> content = queryFactory
				.selectFrom(contractorEntity)
				.leftJoin(contractorEntity.supplyList, supplyEntity)
				.leftJoin(supplyEntity.supplyBookList, supplyBookEntity)
				.leftJoin(supplyBookEntity.bookInfo, bookInfoEntity)
				.where(
						contractorEntity.id.eq(contId)
				)
				.transform(groupBy(contractorEntity.id)
					.list(Projections.fields(ContSupBookRes.class
						, contractorEntity.id.as("contId")
						, contractorEntity.lowest
						, contractorEntity.stateCd
						, contractorEntity.contractAt
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
		
		return content.size() > 0 ? content.get(0) : new ContSupBookRes();
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
	
	private BooleanExpression eqStateCd(String stateCd) {
		return !StringUtils.isEmpty(stateCd) ? contractorEntity.stateCd.eq(stateCd) : null;
	}
	
	private BooleanExpression betweenContractAt(LocalDate startAt, LocalDate endAt) {
		return !ObjectUtils.isEmpty(startAt) && !ObjectUtils.isEmpty(endAt) ? 
					contractorEntity.contractAt.between(startAt, endAt) : null;
	}
}
