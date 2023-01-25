package com.lhj.bookstore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lhj.bookstore.entity.SupplyBookEntity;
import com.lhj.bookstore.entity.SupplyBookId;

@Repository
public interface SupplyBookRepository extends JpaRepository <SupplyBookEntity, SupplyBookId>{

	public List<SupplyBookEntity> findBySupplyId(Long supplyId);

}
