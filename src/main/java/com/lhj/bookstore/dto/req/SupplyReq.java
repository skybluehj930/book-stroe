package com.lhj.bookstore.dto.req;

import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupplyReq {
	
	private Long contId; // 계약번호
	
	private List<Long> bookIds; // 도서번호 목록

	@Builder
	public SupplyReq(Long contId, List<Long> bookIds) {
		this.contId = contId;
		this.bookIds = bookIds;
	}
}
