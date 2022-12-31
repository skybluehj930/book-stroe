package com.lhj.bookstore.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book_info")
public class BookInfoEntity {

	@Id // pk
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id; // 도서번호
	
	private String title; // 도서명
	
	private String type; // 도서구분
	
	private Integer quantity; // 수량
	
	private Integer supPrice; // 공급단가
	
	private String writer; // 저자
	
	private Date createdAt; // 발행일자
	
	private Integer fixPrice; // 정가
	
	private Integer discount; // 적용할인율

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getSupPrice() {
		return supPrice;
	}

	public void setSupPrice(Integer supPrice) {
		this.supPrice = supPrice;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Integer getFixPrice() {
		return fixPrice;
	}

	public void setFixPrice(Integer fixPrice) {
		this.fixPrice = fixPrice;
	}

	public Integer getDiscount() {
		return discount;
	}

	public void setDiscount(Integer discount) {
		this.discount = discount;
	}

	
}
