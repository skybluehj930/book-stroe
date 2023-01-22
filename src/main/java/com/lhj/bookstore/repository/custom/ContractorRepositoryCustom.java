package com.lhj.bookstore.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lhj.bookstore.dto.req.SearchContSupBookReq;
import com.lhj.bookstore.dto.req.SearchContReq;
import com.lhj.bookstore.dto.res.ContSupBookRes;
import com.lhj.bookstore.dto.res.ContractorRes;

public interface ContractorRepositoryCustom {
	Page<ContSupBookRes> searchContSupBook(SearchContSupBookReq searchContSupBookReq, Pageable pageable);
	
	Page<ContractorRes> searchContractor(SearchContReq searchContReq, Pageable pageable);
	
	ContSupBookRes getContSupBook(Long contId);
}
