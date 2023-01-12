package com.lhj.bookstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lhj.bookstore.dto.BookInfoDto;
import com.lhj.bookstore.dto.SearchBookInfoDto;
import com.lhj.bookstore.service.BookInfoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookInfoController {
	
	private final BookInfoService bookInfoService;
	
	@PostMapping("/book")
	public ResponseEntity<Object> registBookInfo(@RequestBody BookInfoDto bookInfoDto) {
		return ResponseEntity.ok(bookInfoService.registBookInfo(bookInfoDto));
	}
	
	@GetMapping("/book")
	public ResponseEntity<Object> searchBookInfo(@ModelAttribute SearchBookInfoDto searchBookInfoDto) {
		return ResponseEntity.ok(bookInfoService.searchBookInfo(searchBookInfoDto));
	}
	
	@PatchMapping("/book/{bookId}")
	public ResponseEntity<Object> modifyBookInfo(@PathVariable long bookId, @RequestBody BookInfoDto bookInfoDto) {
		return ResponseEntity.ok(bookInfoService.modifyBookInfo(bookId, bookInfoDto));
	}
	
	@GetMapping("/book/{bookId}")
	public ResponseEntity<Object> getBookInfo(@PathVariable long bookId) {
		return ResponseEntity.ok(bookInfoService.getBookInfo(bookId));
	}
}
