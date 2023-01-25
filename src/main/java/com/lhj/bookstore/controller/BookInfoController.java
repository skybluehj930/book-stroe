package com.lhj.bookstore.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lhj.bookstore.dto.req.BookInfoReq;
import com.lhj.bookstore.dto.req.SearchBookInfoReq;
import com.lhj.bookstore.dto.res.BookInfoRes;
import com.lhj.bookstore.service.BookInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "도서정보", description = "도서 정보 등록, 검색, 수정 API")
@RestController
@RequiredArgsConstructor
public class BookInfoController {
	
	private final BookInfoService bookInfoService;
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = BookInfoRes.class)))
	})
	@Operation(tags = "도서정보", summary = "도서 등록", description = "")
	@PostMapping("/book")
	public ResponseEntity<Object> registBookInfo(@RequestBody BookInfoReq bookInfoReq) {
		return ResponseEntity.ok(bookInfoService.registBookInfo(bookInfoReq));
	}
	
	@Operation(tags = "도서정보", summary = "도서 검색", description = "")
	@GetMapping("/book")
	public ResponseEntity<Page<BookInfoRes>> searchBookInfo(@ModelAttribute SearchBookInfoReq searchBookInfoReq) {
		return ResponseEntity.ok(bookInfoService.searchBookInfo(searchBookInfoReq));
	}
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = BookInfoRes.class)))
	})
	@Operation(tags = "도서정보", summary = "도서 정보 수정", description = "도서 정보에 supPrice, quantity, fixPrice 값을 수정 한다.")
	@PatchMapping("/book/{bookId}")
	public ResponseEntity<Object> modifyBookInfo(@PathVariable Long bookId, @RequestBody BookInfoReq bookInfoReq) {
		return ResponseEntity.ok(bookInfoService.modifyBookInfo(bookId, bookInfoReq));
	}
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = BookInfoRes.class)))
	})
	@Operation(tags = "도서정보", summary = "도서 단건 조회", description = "")
	@GetMapping("/book/{bookId}")
	public ResponseEntity<Object> getBookInfo(@PathVariable Long bookId) {
		return ResponseEntity.ok(bookInfoService.getBookInfo(bookId));
	}
}
