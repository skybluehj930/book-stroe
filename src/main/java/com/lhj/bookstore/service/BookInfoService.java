package com.lhj.bookstore.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lhj.bookstore.dto.BookInfoDto;
import com.lhj.bookstore.dto.SearchBookInfoDto;
import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.repository.BookInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookInfoService {
	
	private final BookInfoRepository bookInfoRepository;

	@Transactional
	public BookInfoEntity registBookInfo(BookInfoDto bookInfoDto) {
		BookInfoEntity bookInfoEntity = BookInfoEntity.builder()
				.title(bookInfoDto.getTitle())
				.type(bookInfoDto.getType())
				.supPrice(bookInfoDto.getSupPrice())
				.fixPrice(bookInfoDto.getFixPrice())
				.quantity(bookInfoDto.getQuantity())
				.writer(bookInfoDto.getWriter())
				.discount(bookInfoDto.getDiscount())
				.createdAt(LocalDate.parse(bookInfoDto.getCreatedAt(), DateTimeFormatter.ISO_DATE))
				.build();
		
		return bookInfoRepository.save(bookInfoEntity);
	}

	public List<BookInfoEntity> findByTitleContaining(String title) {
		return bookInfoRepository.findByTitleContaining(title);
	}

	public Page<BookInfoEntity> searchBookInfo(SearchBookInfoDto searchBookInfoDto) {
		Pageable pageable = PageRequest.of(searchBookInfoDto.getOffset() -1, searchBookInfoDto.getLimit());
		return bookInfoRepository.searchBookInfo(searchBookInfoDto, pageable);
	}

	@Transactional
	public BookInfoEntity modifyBookInfo(BookInfoDto bookInfoDto) {
		Optional<BookInfoEntity> bookInfoEntity = bookInfoRepository.findById(bookInfoDto.getId());
		if(bookInfoEntity.isPresent()) {
			bookInfoEntity.get().changeDiscount(bookInfoDto.getDiscount());
			bookInfoEntity.get().changeQuantitye(bookInfoDto.getQuantity());
			bookInfoEntity.get().changeSupPrice(bookInfoDto.getSupPrice());
		}
		return bookInfoEntity.get();
	}

	public BookInfoEntity getBookInfo(long id) {
		return bookInfoRepository.findById(id).orElse(null);
	}

}
