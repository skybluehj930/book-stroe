package com.lhj.bookstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookInfoDto {
	
	private String keyword; // 검색 키워드
	
	private String type; // 도서구분
	
	private Integer offset = 1;
	
	private Integer limit = 10;
}
