package com.lhj.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "supply_book")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(SupplyBookId.class)
public class SupplyBookEntity {

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sup_id", nullable = false)
	private SupplyEntity supply; // 공급번호
	
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "book_id", nullable = false)
	private BookInfoEntity bookInfo; // 도서번호
	
	@Builder
	public SupplyBookEntity(SupplyEntity supply, BookInfoEntity bookInfo) {
		if (this.supply != null) {
			this.supply.getSupplyBookList().remove(this);
		}
		this.supply = supply;
		supply.getSupplyBookList().add(this);
		
		this.bookInfo = bookInfo;
	}

}
