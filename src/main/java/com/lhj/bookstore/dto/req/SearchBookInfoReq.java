package com.lhj.bookstore.dto.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // @ModelAttribute로 값을 받기 위해 setter 적용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchBookInfoReq {
	
	@ApiModelProperty(required = false, value = "도서명 or 저자명에 포함 되는 키워드", example = "d")
	private String keyword;
	
	@ApiModelProperty(required = false, value = "T001", allowableValues = "T001, T002, T003")
	private String type;
	
	@ApiModelProperty(required = true, value = "1")
	private Integer offset = 1;
	
	@ApiModelProperty(required = true, value = "10")
	private Integer limit = 10;
	
	@Builder
	public SearchBookInfoReq(String keyword, String type, Integer offset, Integer limit) {
		this.keyword = keyword;
		this.type = type;
		this.offset = offset;
		this.limit = limit;
	}
}
