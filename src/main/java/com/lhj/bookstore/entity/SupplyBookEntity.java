package com.lhj.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "supply_book")
public class SupplyBookEntity {

	@Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name = "supply_id")
	private SupplyEntity supply; // 공급번호
	
	@ManyToOne
	@JoinColumn(name = "book_info_id")
	private BookInfoEntity bookInfo; // 도서번호

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SupplyEntity getSupply() {
		return supply;
	}

	public void setSupply(SupplyEntity supply) {
		this.supply = supply;
	}

	public BookInfoEntity getBookInfo() {
		return bookInfo;
	}

	public void setBookInfo(BookInfoEntity bookInfo) {
		this.bookInfo = bookInfo;
	}
	
}
