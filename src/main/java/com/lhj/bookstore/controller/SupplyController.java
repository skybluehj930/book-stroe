package com.lhj.bookstore.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lhj.bookstore.dto.req.SearchSupBookReq;
import com.lhj.bookstore.dto.req.SupplyReq;
import com.lhj.bookstore.dto.res.SupBookRes;
import com.lhj.bookstore.dto.res.SupplyRes;
import com.lhj.bookstore.service.SupplyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "공급내역", description = "개약 업체에 공급되는 도서를 등록, 공급 내역을 검색, 삭제 하는 API")
@RestController
@RequiredArgsConstructor
public class SupplyController {
	
	private final SupplyService supplyService;

	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = SupplyRes.class)))
	})
	@Operation(tags = "공급내역", summary = "공급내역 등록", description = "개약 업체에 공급되는 도서를 등록")
	@PostMapping("/supply")
	public ResponseEntity<Object> registSupply(@RequestBody SupplyReq supplyReq) {
		return ResponseEntity.ok(supplyService.registSupply(supplyReq));
	}
	
	@Operation(tags = "공급내역", summary = "공급 도서 검색", description = "")
	@GetMapping("/supply")
	public ResponseEntity<Page<SupBookRes>> searchSupplyBook(@ModelAttribute SearchSupBookReq searchSupBookReq) {
		return ResponseEntity.ok(supplyService.searchSupplyBook(searchSupBookReq));
	}
	
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "요청 성공", content = @Content(schema = @Schema(implementation = SupplyRes.class)))
	})
	@Operation(tags = "공급내역", summary = "공급내역 삭제", description = "")
	@DeleteMapping("/supply/{supId}")
	public ResponseEntity<Object> removeSupply(@PathVariable Long supId) {
		return ResponseEntity.ok(supplyService.removeSupply(supId));
	}
}
