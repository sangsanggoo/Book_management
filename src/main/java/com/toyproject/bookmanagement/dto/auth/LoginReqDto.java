package com.toyproject.bookmanagement.dto.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class LoginReqDto {
	@Email
	private String email;
	
	// 비밀번호 정규식(최소 8글자, 글자 1개, 숫자 1개, 특수문자 1개)
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
			message = "비밀번호는 영문자, 숫자, 특수문자를 포함하여 8~16글자로 작성")
	private String password;
	
}
