package com.lhj.bookstore.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lhj.bookstore.dto.SearchBookInfoDto;
import com.lhj.bookstore.entity.BookInfoEntity;

public interface BookInfoRepositoryCustom {
	Page<BookInfoEntity> searchBookInfo(SearchBookInfoDto booInfoDto, Pageable pageable);
}
