package com.lhj.bookstore.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lhj.bookstore.dto.req.SearchBookInfoReq;
import com.lhj.bookstore.dto.res.BookInfoRes;

public interface BookInfoRepositoryCustom {
	Page<BookInfoRes> searchBookInfo(SearchBookInfoReq searchBookInfoReq, Pageable pageable);

	BookInfoRes getBookInfo(Long bookId);
}
