package com.lhj.bookstore.mapper;

import com.lhj.bookstore.dto.req.BookInfoReq;
import com.lhj.bookstore.dto.res.BookInfoRes;
import com.lhj.bookstore.entity.BookInfoEntity;

public interface BookInfoMapper {

	default BookInfoRes entityToDto(BookInfoEntity bookInfoEntity) {
		return BookInfoRes.builder()
				.id(bookInfoEntity.getId())
				.title(bookInfoEntity.getTitle())
				.type(bookInfoEntity.getType())
				.supPrice(bookInfoEntity.getSupPrice())
				.fixPrice(bookInfoEntity.getFixPrice())
				.quantity(bookInfoEntity.getQuantity())
				.writer(bookInfoEntity.getWriter())
				.discount(bookInfoEntity.getDiscount())
				.createdAt(bookInfoEntity.getCreatedAt())
				.build();
	}
	
	default BookInfoEntity dtoToEntity(BookInfoReq bookInfoReq) {
		return BookInfoEntity.builder()
				.title(bookInfoReq.getTitle())
				.type(bookInfoReq.getType())
				.supPrice(bookInfoReq.getSupPrice())
				.fixPrice(bookInfoReq.getFixPrice())
				.quantity(bookInfoReq.getQuantity())
				.writer(bookInfoReq.getWriter())
				.discount(bookInfoReq.getDiscount())
				.createdAt(bookInfoReq.getCreatedAt())
				.build();
	}
}
