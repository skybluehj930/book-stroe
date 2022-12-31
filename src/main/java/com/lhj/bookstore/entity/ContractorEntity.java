package com.lhj.bookstore.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "contractor")
public class ContractorEntity {

	@Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 계약번호
	
	private Date contractAt; // 계약일자
	
	private Integer lowest; // 최저가 비율
	
	private String stateCd; // 상태 코드

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getContractAt() {
		return contractAt;
	}

	public void setContractAt(Date contractAt) {
		this.contractAt = contractAt;
	}

	public Integer getLowest() {
		return lowest;
	}

	public void setLowest(Integer lowest) {
		this.lowest = lowest;
	}

	public String getStateCd() {
		return stateCd;
	}

	public void setStateCd(String stateCd) {
		this.stateCd = stateCd;
	}
	
	
}
