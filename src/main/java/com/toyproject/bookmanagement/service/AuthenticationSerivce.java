package com.toyproject.bookmanagement.service;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.dto.auth.JwtRespDto;
import com.toyproject.bookmanagement.dto.auth.LoginReqDto;
import com.toyproject.bookmanagement.dto.auth.PrincipalRespDto;
import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.entity.Authority;
import com.toyproject.bookmanagement.entity.User;
import com.toyproject.bookmanagement.exception.CustomException;
import com.toyproject.bookmanagement.exception.ErrorMap;
import com.toyproject.bookmanagement.repository.UserRepository;
import com.toyproject.bookmanagement.security.jwtTokenProvider;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationSerivce implements UserDetailsService{
	private final UserRepository userRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final jwtTokenProvider jwtTokenProvider;
	
	
	//email 중복 제거
	public void checkduplicatedEmail(String email) {
		if (userRepository.findUserByEmail(email) != null) {
			throw new CustomException("duplicated", ErrorMap.builder().put("email", "사용중인 email입니다.").build());
		}
	}
	
	public void signup(SignupReqDto signupReqDto) {
		User userEntity = signupReqDto.toentity();
		userRepository.saveUser(userEntity);
		userRepository.saveAuthority(Authority.builder().userId(userEntity.getUserId()).roleId(1).build());
	}
	
	
	public JwtRespDto signin(LoginReqDto loginReqDto) {
		UsernamePasswordAuthenticationToken passwordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(loginReqDto.getEmail(),loginReqDto.getPassword());
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(passwordAuthenticationToken);
		return jwtTokenProvider.generateToken(authentication);
	}
	
	
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userEntity = userRepository.findUserByEmail(username);
		if(userEntity == null) {
			throw new CustomException("로그인 실패",ErrorMap.builder().put("email","사용자 정보를 확인하세요").build());
		}
		return userEntity.toPrincipal();
	}
	
	public boolean authenticated(String accessToken) {
//		System.out.println(accessToken);
		return jwtTokenProvider.validateToken(jwtTokenProvider.getToken(accessToken));
		}
	
	public PrincipalRespDto getPrincipal(String accessToken) {
		Claims claims = jwtTokenProvider.getClaims(jwtTokenProvider.getToken(accessToken));
		User userEntity =  userRepository.findUserByEmail(claims.getSubject());
		return PrincipalRespDto.builder()
				.userId(userEntity.getUserId())
				.email(userEntity.getEmail())
				.name(userEntity.getName())
				.authorities((String)claims.get("auth"))
				.build();
				
	}
	
}
