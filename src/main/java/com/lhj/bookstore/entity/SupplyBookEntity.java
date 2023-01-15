package com.lhj.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class SupplyBookEntity {
	
	@Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 공급도서번호

	@ManyToOne
	@JoinColumn(name = "sup_id")
	private SupplyEntity supply; // 공급번호
	
	@ManyToOne
	@JoinColumn(name = "book_id")
	private BookInfoEntity bookInfo; // 도서번호
	
	@Builder
	public SupplyBookEntity(SupplyEntity supply, BookInfoEntity bookInfo) {
		this.supply = supply;
		this.bookInfo = bookInfo;
	}

}
