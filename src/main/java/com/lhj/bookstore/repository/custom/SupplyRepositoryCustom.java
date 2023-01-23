package com.lhj.bookstore.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lhj.bookstore.dto.req.SearchSupBookReq;
import com.lhj.bookstore.dto.res.SupBookRes;

public interface SupplyRepositoryCustom {
	
	Page<SupBookRes> searchSupplyBook(SearchSupBookReq searchSupBookReq, Pageable pageable);
}
