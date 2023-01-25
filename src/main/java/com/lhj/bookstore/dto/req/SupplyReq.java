package com.lhj.bookstore.dto.req;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupplyReq {
	
	@Schema(description = "계약번호", required = true, example = "1")
	private Long contId; // 계약번호
	
	@Schema(description = "도서번호 목록", required = true)
	private List<Long> bookIds; // 도서번호 목록

	@Builder
	public SupplyReq(Long contId, List<Long> bookIds) {
		this.contId = contId;
		this.bookIds = bookIds;
	}
}
