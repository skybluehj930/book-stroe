package com.lhj.bookstore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lhj.bookstore.dto.ContractorDto;
import com.lhj.bookstore.service.ContractorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ContractorController {
	
	private final ContractorService contractorService;
	
	@PostMapping("/contractor")
	public ResponseEntity<Object> registContractor(@RequestBody ContractorDto contractorDto) {
		return ResponseEntity.ok(contractorService.registContractor(contractorDto));
	}
	
	@GetMapping("/contractor")
	public ResponseEntity<Object> findContractor(@RequestParam Integer offset, @RequestParam Integer limit) {
		return ResponseEntity.ok(contractorService.findContractor(offset, limit));
	}
	
	@PatchMapping("/contractor/{conId}")
	public ResponseEntity<Object> changeContractor(@PathVariable Long conId, @RequestBody ContractorDto contractorDto) {
		return ResponseEntity.ok(contractorService.modifyContractor(conId, contractorDto));
	}
}
