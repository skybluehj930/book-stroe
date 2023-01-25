package com.lhj.bookstore.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lhj.bookstore.dto.req.ContractorReq;
import com.lhj.bookstore.dto.req.SearchContReq;
import com.lhj.bookstore.dto.req.SearchContSupBookReq;
import com.lhj.bookstore.dto.res.ContSupBookRes;
import com.lhj.bookstore.dto.res.ContractorRes;
import com.lhj.bookstore.service.ContractorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "계약업체", description = "계약업체 등록, 검색, 내용 변경 API")
@RestController
@RequiredArgsConstructor
public class ContractorController {
	
	private final ContractorService contractorService;
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = ContractorRes.class)))
	})
	@Operation(tags = "계약업체", summary = "계약업체 등록", description = "")
	@PostMapping("/contractor")
	public ResponseEntity<Object> registContractor(@RequestBody ContractorReq contractorReq) {
		return ResponseEntity.ok(contractorService.registContractor(contractorReq));
	}
	
	@Operation(tags = "계약업체", summary = "계약업체 검색", description = "")
	@GetMapping("/contractor")
	public ResponseEntity<Page<ContractorRes>> searchContractor(@ModelAttribute SearchContReq searchContReq) {
		return ResponseEntity.ok(contractorService.searchContractor(searchContReq));
	}
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = ContractorRes.class)))
	})
	@Operation(tags = "계약업체", summary = "계약업체 내용 변경", description = "")
	@PutMapping("/contractor/{contId}")
	public ResponseEntity<Object> changeContractor(@PathVariable Long contId, @RequestBody ContractorReq contractorReq) {
		return ResponseEntity.ok(contractorService.changeContractor(contId, contractorReq));
	}
	
	@Operation(tags = "계약업체", summary = "계약업체별 공급된 도서 검색", description = "")
	@GetMapping("/contractor/supply-book")
	public ResponseEntity<Page<ContSupBookRes>> searchContSupBook(@ModelAttribute SearchContSupBookReq searchContSupBookReq) {
		return ResponseEntity.ok(contractorService.searchContSupBook(searchContSupBookReq));
	}
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = ContSupBookRes.class)))
	})
	@Operation(tags = "계약업체", summary = "계약업체 공급된 도서와 상관 없이 단건 조회", description = "공급된 도서가 있으면 나오고 없으면 안나온다.")
	@GetMapping("/contractor/supply-book/{contId}")
	public ResponseEntity<Object> searchContSupBook(@PathVariable Long contId) {
		return ResponseEntity.ok(contractorService.getContSupBook(contId));
	}
}
