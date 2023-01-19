package com.lhj.bookstore.dto.res;

import java.util.ArrayList;
import java.util.List;

import com.lhj.bookstore.dto.BookInfoDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class ContSupBookRes {
	private Long contId; // 계약번호
	
	private Integer lowest; // 최저가 비율
	
	private String stateCd; // 상태 코드
	
	List<BookInfoDto> bookInfo = new ArrayList<BookInfoDto>();
	
	private Integer offset = 1;
	
	private Integer limit = 10;
	
}
