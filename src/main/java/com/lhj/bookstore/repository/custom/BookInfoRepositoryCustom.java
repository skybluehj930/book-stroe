package com.lhj.bookstore.repository.custom;

import java.util.List;

import com.lhj.bookstore.entity.BookInfoEntity;

public interface BookInfoRepositoryCustom {
	List<BookInfoEntity> searchBookInfo();
}
