package com.lhj.bookstore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.repository.BookInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookStoreService {
	
	private final BookInfoRepository bookInfoRepository;

	public BookInfoEntity bookRegister(BookInfoEntity bookInfoEntity) {
		// TODO Auto-generated method stub
		return bookInfoRepository.save(bookInfoEntity);
	}

	public List<BookInfoEntity> findByTitleContaining(String title) {
		// TODO Auto-generated method stub
		return bookInfoRepository.findByTitleContaining(title);
	}

}
