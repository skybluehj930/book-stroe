package com.lhj.bookstore.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lhj.bookstore.dto.ContractorDto;
import com.lhj.bookstore.entity.ContractorEntity;
import com.lhj.bookstore.repository.ContractorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractorService {
	
	private final ContractorRepository contractorRepository;

	@Transactional
	public ContractorEntity registContractor(ContractorDto contractorDto) {
		ContractorEntity contractorEntity = ContractorEntity.builder()
				.contractAt(LocalDate.parse(contractorDto.getContractAt(), DateTimeFormatter.ISO_DATE))
				.lowest(contractorDto.getLowest())
				.stateCd(contractorDto.getStateCd())
				.build();
		return contractorRepository.save(contractorEntity);
	}

	public Page<ContractorEntity> findContractor(int offset, int limit) {
		Pageable pageable = PageRequest.of(offset -1, limit);
		return contractorRepository.findAll(pageable);
	}

	@Transactional
	public ContractorEntity modifyContractor(long conId, ContractorDto contractorDto) {
		Optional<ContractorEntity> contractorEntity = contractorRepository.findById(conId);
		if(contractorEntity.isPresent()) {
			contractorEntity.get().changeLowest(contractorDto.getLowest());
			contractorEntity.get().changeStateCd(contractorDto.getStateCd());
		}
		return contractorEntity.get();
	}

}
