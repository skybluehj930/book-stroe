package com.lhj.bookstore.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "supply")
public class SupplyEntity {
	
	@Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 공급번호
	
	@ManyToOne
	@JoinColumn(name = "contractor_id")
	private ContractorEntity contractor; // 계약번호
	
	private Date supplyAt; // 공급일자

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ContractorEntity getContractor() {
		return contractor;
	}

	public void setContractor(ContractorEntity contractor) {
		this.contractor = contractor;
	}

	public Date getSupplyAt() {
		return supplyAt;
	}

	public void setSupplyAt(Date supplyAt) {
		this.supplyAt = supplyAt;
	}

	
}
