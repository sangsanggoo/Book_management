package com.toyproject.bookmanagement.dto.book;

import lombok.Builder;
import lombok.Data;

@Data
public class SearchBookReqDto {
	private int page;
	private String searchValue;
	private int catetoryId;
}
