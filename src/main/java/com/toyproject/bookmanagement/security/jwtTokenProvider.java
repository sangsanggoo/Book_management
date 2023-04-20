package com.toyproject.bookmanagement.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class jwtTokenProvider {
	private Key key;
	
	public jwtTokenProvider(@Value("${jwt.secret}") String secretKey) { //apllication.yml의 secret키 들고 오는거
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}
	public JwtRespDto generateToken(Authentication authentication) {
		
		Date tokenExpiresDate = new Date(new Date().getTime()+(1000 * 60 * 60 * 24)); // 현재 시간 + 하루 (1000이 1초)
		
		
		// authentication의 authorities를 빼서 StringBuilder로 한줄로 합친뒤
		// authorities 변수에 넣어주고 token내용에 추가
		String authorities = null;
		StringBuilder builder = new StringBuilder();
		
		authentication.getAuthorities().forEach(authority -> {
			builder.append(authority.getAuthority() + ",");
		});
		 builder.delete(builder.length()-1, builder.length());
		 authorities =builder.toString();
		
		
		String accessToken = Jwts.builder()
				.setSubject(authentication.getName()) 						//토큰의 제목
				.claim("auth",authorities)						// auth
				.setExpiration(tokenExpiresDate)					//만료 시간
				.signWith(key,SignatureAlgorithm.HS256) //토큰 암호화
				.compact();
		return JwtRespDto.builder().grantType("Bearer").accessToken(accessToken).build();
	}
}
