package com.toyproject.bookmanagement.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
@Slf4j
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
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT Token",e);
		} catch ( ExpiredJwtException e){
			log.info("expired Jwt Token",e);
		} catch (UnsupportedJwtException e){
			log.info("Unsupported JWT Token",e);
		} catch (IllegalArgumentException e) {
			log.info("IllegalArgument JWT Token",e);
		} catch ( Exception e) {
			log.info("JWT Token Error",e);
		}
		return false;
	}
	
	public String getToken(String token) {
		String type = "Bearer";
//		System.out.println(token);
		if(StringUtils.hasText(token) && token.startsWith(type)) {
			return token.substring(type.length()+1);
		}
		return null;
		
	}
	
	public Claims getClaims(String tokken) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(tokken)
				.getBody();
	}
}
