package com.lhj.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lhj.bookstore.entity.SupplyBookEntity;

@Repository
public interface SupplyBookRepository extends JpaRepository <SupplyBookEntity, Long>{

	public List<SupplyBookEntity> findBySupplyId(Long supplyId);

}
