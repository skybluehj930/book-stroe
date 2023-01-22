package com.lhj.bookstore.dto.req;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // @ModelAttribute로 값을 받기 위해 setter 적용
@NoArgsConstructor
public class SearchBookInfoReq {
	
	private String keyword; // 검색 키워드
	
	private String type; // 도서구분
	
	private Integer offset = 1;
	
	private Integer limit = 10;
	
	@Builder
	public SearchBookInfoReq(String keyword, String type, Integer offset, Integer limit) {
		this.keyword = keyword;
		this.type = type;
		this.offset = offset;
		this.limit = limit;
	}
}
