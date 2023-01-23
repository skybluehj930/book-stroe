package com.lhj.bookstore.dto.res;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SupBookRes {
	
	private Long supId; // 공급번호
	
	private LocalDate supplyAt; // 공급일자
	
	List<BookInfoRes> bookInfo = new ArrayList<BookInfoRes>();
}
