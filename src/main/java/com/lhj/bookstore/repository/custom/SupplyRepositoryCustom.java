package com.lhj.bookstore.repository.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lhj.bookstore.dto.SearchSupplyBookDto;
import com.lhj.bookstore.dto.SupplyBookDto;

public interface SupplyRepositoryCustom {
	List<SupplyBookDto> getSupplyBooks(Long supId);
	
	Page<SearchSupplyBookDto> searchSupplyBook(SearchSupplyBookDto searchSupplyBookDto, Pageable pageable);
}
