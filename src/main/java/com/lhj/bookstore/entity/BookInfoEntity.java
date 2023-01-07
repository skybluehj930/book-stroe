package com.lhj.bookstore.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_info")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookInfoEntity {

	@Id // pk
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long id; // 도서번호
	
	private String title; // 도서명
	
	private String type; // 도서구분
	
	private Integer quantity; // 수량
	
	private Integer supPrice; // 공급단가
	
	private String writer; // 저자
	
	private LocalDateTime createdAt; // 발행일자
	
	private Integer fixPrice; // 정가
	
	private Integer discount; // 적용할인율
	
	
	public void changeSupPrice(Integer supPrice) {
		this.supPrice = supPrice;
	}
	
	public void changeQuantitye(Integer quantity) {
		this.quantity = quantity;
	}
	
	public void changeDiscount(Integer discount) {
		this.discount = discount;
	}
	
	@Builder
	public BookInfoEntity(String title
						, String type 
						, Integer quantity
						, Integer supPrice 
						, String writer
						, Integer fixPrice
						, Integer discount
						, LocalDateTime createdAt) {
		this.title = title;
		this.type = type;
		this.quantity = quantity;
		this.supPrice = supPrice;
		this.writer = writer;
		this.fixPrice = fixPrice;
		this.discount = discount;
		this.createdAt = createdAt;
	}
}
