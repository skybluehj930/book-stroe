package com.lhj.bookstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lhj.bookstore.entity.SupplyEntity;

@Repository
public interface SupplyRepository extends JpaRepository <SupplyEntity, Long> {

}
