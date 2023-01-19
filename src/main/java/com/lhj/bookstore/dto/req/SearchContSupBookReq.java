package com.lhj.bookstore.dto.req;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // @ModelAttribute로 값을 받기 위해 setter 적용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchContSupBookReq {
	
	private String stateCd; // 상태 코드
	
	private String title; // 도서명
	
	private String type; // 도서구분
	
	private String writer; // 저자
	
	private Integer offset;
	
	private Integer limit;
	
	@Builder
	public SearchContSupBookReq(
					String stateCd
					, String title
					, String type
					, String writer
					, Integer offset
					, Integer limit) {
		
		this.stateCd = stateCd;
		this.title = title;
		this.type = type;
		this.writer = writer;
		this.offset = offset;
		this.limit = limit;
	}
}
