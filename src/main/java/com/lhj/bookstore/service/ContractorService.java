package com.lhj.bookstore.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lhj.bookstore.dto.req.ContractorReq;
import com.lhj.bookstore.dto.req.SearchContReq;
import com.lhj.bookstore.dto.req.SearchContSupBookReq;
import com.lhj.bookstore.dto.res.ContSupBookRes;
import com.lhj.bookstore.dto.res.ContractorRes;
import com.lhj.bookstore.entity.ContractorEntity;
import com.lhj.bookstore.mapper.ContractorMapper;
import com.lhj.bookstore.repository.ContractorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractorService implements ContractorMapper {
	
	private final ContractorRepository contractorRepository;

	@Transactional
	public ContractorRes registContractor(ContractorReq contractorReq) {
		ContractorEntity contractorEntity = dtoToEntity(contractorReq);
		return entityToDto(contractorRepository.save(contractorEntity));
	}

	public Page<ContractorRes> searchContractor(SearchContReq searchContReq) {
		Pageable pageable = PageRequest.of(searchContReq.getOffset() -1, searchContReq.getLimit());
		return contractorRepository.searchContractor(searchContReq, pageable);
	}

	@Transactional
	public ContractorRes modifyContractor(Long conId, ContractorReq contractorReq) {
		Optional<ContractorEntity> contractorEntity = contractorRepository.findById(conId);
		if(contractorEntity.isPresent()) {
			contractorEntity.get().changeLowest(contractorReq.getLowest());
			contractorEntity.get().changeStateCd(contractorReq.getStateCd());
		}
		return entityToDto(contractorEntity.get());
	}

	public Page<ContSupBookRes> searchContSupBook(SearchContSupBookReq searchContSupBookReq) {
		Pageable pageable = PageRequest.of(searchContSupBookReq.getOffset() -1, searchContSupBookReq.getLimit());
		return contractorRepository.searchContSupBook(searchContSupBookReq, pageable);
	}

	public ContSupBookRes getContSupBook(Long contId) {
		return contractorRepository.getContSupBook(contId);
	}
}
