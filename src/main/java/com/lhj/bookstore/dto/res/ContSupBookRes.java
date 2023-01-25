package com.lhj.bookstore.dto.res;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ContSupBookRes {
	
	@Schema(description = "계약번호", required = true, example = "1")
	private Long contId;
	
	@Schema(description = "최저가 비율", required = true, example = "10")
	private Integer lowest;
	
	@Schema(description = "상태 코드", required = true, example = "A", allowableValues = {"A", "B", "C"})
	private String stateCd;
	
	@Schema(description = "계약일자", required = true, example = "2023-01-25")
	private LocalDate contractAt;
	
	List<BookInfoRes> bookInfo = new ArrayList<BookInfoRes>();
}
