package com.lhj.bookstore.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supply")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupplyEntity {
	
	@Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 공급번호
	
	@ManyToOne
	@JoinColumn(name = "cont_id")
	private ContractorEntity contractor; // 계약번호
	
	@OneToMany(mappedBy = "supply")
	private List<SupplyBookEntity> supplyBookList = new ArrayList<>();
	
	private LocalDate supplyAt; // 공급일자
	
	public void deleteSupCont() {
		if(this.contractor != null) {
			contractor.getSupplyList().remove(this);
			this.contractor = null;
		}
	}
	
	@Builder
	public SupplyEntity(ContractorEntity contractor, LocalDate supplyAt) {
		if (this.contractor != null) {
			this.contractor.getSupplyList().remove(this);
		}
		this.contractor = contractor;
		contractor.getSupplyList().add(this);
		
		this.supplyAt =  supplyAt;
	}
}
