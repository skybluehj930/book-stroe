package com.lhj.bookstore.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lhj.bookstore.dto.req.SearchSupBookReq;
import com.lhj.bookstore.dto.req.SupplyReq;
import com.lhj.bookstore.dto.res.SupBookRes;
import com.lhj.bookstore.dto.res.SupplyRes;
import com.lhj.bookstore.entity.BookInfoEntity;
import com.lhj.bookstore.entity.ContractorEntity;
import com.lhj.bookstore.entity.SupplyBookEntity;
import com.lhj.bookstore.entity.SupplyEntity;
import com.lhj.bookstore.repository.BookInfoRepository;
import com.lhj.bookstore.repository.ContractorRepository;
import com.lhj.bookstore.repository.SupplyBookRepository;
import com.lhj.bookstore.repository.SupplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SupplyService {
	
	private final SupplyRepository supplyRepository;
	
	private final SupplyBookRepository supplyBookRepository;
	
	private final ContractorRepository contractorRepository;
	
	private final BookInfoRepository bookInfoRepository;
	
	@Transactional
	public SupplyRes registSupply(SupplyReq supplyReq) {
		Optional<ContractorEntity> contractor = contractorRepository.findById(supplyReq.getContId());
		
		if (contractor.isPresent()) {
			SupplyEntity supply = supplyRepository.save(SupplyEntity.builder()
					.contractor(contractor.get())
					.supplyAt(LocalDate.now())
					.build());
			
			for (Long bookId : supplyReq.getBookIds()) {
				Optional<BookInfoEntity> bookInfo = bookInfoRepository.findById(bookId);
				if (bookInfo.isPresent()) {
					supplyBookRepository.save(SupplyBookEntity.builder()
							.supply(supply)
							.bookInfo(bookInfo.get())
							.build());
				} else {
					throw new NullPointerException("not found bookId: " + bookId);
				}
			}
			
			return SupplyRes.builder()
					.supId(supply.getId())
					.supplyAt(supply.getSupplyAt())
					.build();
		}
		throw new NullPointerException("not found contractor");
	}

	public Page<SupBookRes> searchSupplyBook(SearchSupBookReq searchSupBookReq) {
		Pageable pageable = PageRequest.of(searchSupBookReq.getOffset() -1, searchSupBookReq.getLimit());
		return supplyRepository.searchSupplyBook(searchSupBookReq, pageable);
	}

	public Long removeSupply(Long supId) {
		Optional<SupplyEntity> supply = supplyRepository.findById(supId);
		if (supply.isPresent()) {
			supply.get().getSupplyBookList().forEach(supplyBookRepository::delete);
			supply.get().deleteSupCont();
			supplyRepository.delete(supply.get());
			return supId;
		}
		throw new NullPointerException("not found supply");
	}
}
