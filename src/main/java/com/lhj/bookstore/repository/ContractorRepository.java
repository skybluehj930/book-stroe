package com.lhj.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lhj.bookstore.entity.ContractorEntity;
import com.lhj.bookstore.repository.custom.ContractorRepositoryCustom;

public interface ContractorRepository extends JpaRepository <ContractorEntity, Long>, ContractorRepositoryCustom {

	public List<ContractorEntity> findByStateCd(String stateCd);

}
