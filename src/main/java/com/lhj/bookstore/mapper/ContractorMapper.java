package com.lhj.bookstore.mapper;

import com.lhj.bookstore.dto.req.ContractorReq;
import com.lhj.bookstore.dto.res.ContractorRes;
import com.lhj.bookstore.entity.ContractorEntity;

public interface ContractorMapper {

	default ContractorRes entityToDto(ContractorEntity contractorEntity) {
		return ContractorRes.builder()
				.id(contractorEntity.getId())
				.contractAt(contractorEntity.getContractAt())
				.lowest(contractorEntity.getLowest())
				.stateCd(contractorEntity.getStateCd())
				.build();
	}
	
	default ContractorEntity dtoToEntity(ContractorReq contractorReq) {
		return ContractorEntity.builder()
				.contractAt(contractorReq.getContractAt())
				.lowest(contractorReq.getLowest())
				.stateCd(contractorReq.getStateCd())
				.build();
	}
}
