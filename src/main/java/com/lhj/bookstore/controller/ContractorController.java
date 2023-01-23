package com.lhj.bookstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.lhj.bookstore.dto.req.ContractorReq;
import com.lhj.bookstore.dto.req.SearchContReq;
import com.lhj.bookstore.dto.req.SearchContSupBookReq;
import com.lhj.bookstore.service.ContractorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ContractorController {
	
	private final ContractorService contractorService;
	
	@PostMapping("/contractor")
	public ResponseEntity<Object> registContractor(@RequestBody ContractorReq contractorReq) {
		return ResponseEntity.ok(contractorService.registContractor(contractorReq));
	}
	
	@GetMapping("/contractor")
	public ResponseEntity<Object> findContractor(@ModelAttribute SearchContReq searchContReq) {
		return ResponseEntity.ok(contractorService.searchContractor(searchContReq));
	}
	
	@PatchMapping("/contractor/{conId}")
	public ResponseEntity<Object> changeContractor(@PathVariable Long conId, @RequestBody ContractorReq contractorReq) {
		return ResponseEntity.ok(contractorService.modifyContractor(conId, contractorReq));
	}
	
	@GetMapping("/contractor/supply-book")
	public ResponseEntity<Object> searchContSupBook(@ModelAttribute SearchContSupBookReq searchContSupBookReq) {
		return ResponseEntity.ok(contractorService.searchContSupBook(searchContSupBookReq));
	}
	
	@GetMapping("/contractor/supply-book/{contId}")
	public ResponseEntity<Object> searchContSupBook(@PathVariable Long contId) {
		return ResponseEntity.ok(contractorService.getContSupBook(contId));
	}
}
