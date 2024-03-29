package com.lhj.bookstore.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhj.bookstore.dto.req.BookInfoReq;
import com.lhj.bookstore.dto.req.SearchBookInfoReq;
import com.lhj.bookstore.dto.res.BookInfoRes;
import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.repository.BookInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookInfoService {
	
	private final BookInfoRepository bookInfoRepository;

	@Transactional
	public BookInfoRes registBookInfo(BookInfoReq bookInfoReq) {
		BookInfoEntity bookInfoEntity = dtoToEntity(bookInfoReq);
		return entityToDto(bookInfoRepository.save(bookInfoEntity));
	}

	@Transactional(readOnly = true)
	public Page<BookInfoRes> searchBookInfo(SearchBookInfoReq searchBookInfoReq) {
		Pageable pageable = PageRequest.of(searchBookInfoReq.getOffset() -1, searchBookInfoReq.getLimit());
		return bookInfoRepository.searchBookInfo(searchBookInfoReq, pageable);
	}

	@Transactional
	public BookInfoRes modifyBookInfo(Long bookId, BookInfoReq bookInfoReq) {
		Optional<BookInfoEntity> bookInfoEntity = bookInfoRepository.findById(bookId);
		if(bookInfoEntity.isPresent()) {
			bookInfoEntity.get().changeDiscount(bookInfoReq.getDiscount());
			bookInfoEntity.get().changeQuantitye(bookInfoReq.getQuantity());
			bookInfoEntity.get().changeSupPrice(bookInfoReq.getSupPrice());
		}
		return entityToDto(bookInfoEntity.get());
	}

	@Transactional(readOnly = true)
	public BookInfoRes getBookInfo(Long bookId) {
		return bookInfoRepository.getBookInfo(bookId);
	}

	private BookInfoRes entityToDto(BookInfoEntity bookInfoEntity) {
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
	
	private BookInfoEntity dtoToEntity(BookInfoReq bookInfoReq) {
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
