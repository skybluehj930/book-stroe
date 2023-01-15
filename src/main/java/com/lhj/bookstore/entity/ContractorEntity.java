package com.lhj.bookstore.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contractor")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContractorEntity {

	@Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "con_id")
    private Long id; // 계약번호
	
	private LocalDate contractAt; // 계약일자
	
	private Integer lowest; // 최저가 비율
	
	private String stateCd; // 상태 코드
	
	@OneToMany(mappedBy = "contractor")
	private List<SupplyEntity> supplyList = new ArrayList<>();
	
	public void changeLowest(Integer lowest) {
		this.lowest = lowest;
	}
	
	public void changeStateCd(String stateCd) {
		this.stateCd = stateCd;
	}

	@Builder
	public ContractorEntity(LocalDate contractAt, Integer lowest, String stateCd) {
		this.contractAt = contractAt;
		this.lowest = lowest;
		this.stateCd = stateCd;
	}
}
