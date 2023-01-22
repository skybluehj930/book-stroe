package com.lhj.bookstore.dto.res;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContSupBookRes {
	private Long contId; // 계약번호
	
	private Integer lowest; // 최저가 비율
	
	private String stateCd; // 상태 코드
	
	private LocalDate contractAt; // 계약일자
	
	List<BookInfoRes> bookInfo = new ArrayList<BookInfoRes>();
}
