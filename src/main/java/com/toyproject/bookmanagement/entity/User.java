package com.toyproject.bookmanagement.entity;

import java.util.ArrayList;
import java.util.List;

import com.toyproject.bookmanagement.security.PrinciplaUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class User {
	private int userId;
	private String email;
	private String password;
	private String name;
	private String Provider;
	
	private List<Authority> authorities;
	
	public PrinciplaUser toPrincipal( ) {

		return PrinciplaUser.builder()
				.userId(userId)
				.email(email)
				.password(password)
				.authorities(authorities)
				.build();
	}
}	
