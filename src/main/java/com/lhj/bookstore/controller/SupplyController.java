package com.lhj.bookstore.controller;

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
import com.lhj.bookstore.service.SupplyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SupplyController {
	
	private final SupplyService supplyService;

	@PostMapping("/supply")
	public ResponseEntity<Object> registSupply(@RequestBody SupplyReq supplyReq) {
		return ResponseEntity.ok(supplyService.registSupply(supplyReq));
	}
	
	@GetMapping("/supply")
	public ResponseEntity<Object> searchSupplyBook(@ModelAttribute SearchSupBookReq searchSupBookReq) {
		return ResponseEntity.ok(supplyService.searchSupplyBook(searchSupBookReq));
	}
	
	@DeleteMapping("/supply/{supId}")
	public ResponseEntity<Object> removeSupply(@PathVariable Long supId) {
		return ResponseEntity.ok(supplyService.removeSupply(supId));
	}
}
