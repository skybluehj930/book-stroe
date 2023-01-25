package com.lhj.bookstore.dto.res;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SupBookRes {
	
	@Schema(description = "공급번호", required = true, example = "1")
	private Long supId;
	
	@Schema(description = "공급일자", required = true, example = "2023-01-25")
	private LocalDate supplyAt; // 공급일자
	
	List<BookInfoRes> bookInfo = new ArrayList<BookInfoRes>();
}
