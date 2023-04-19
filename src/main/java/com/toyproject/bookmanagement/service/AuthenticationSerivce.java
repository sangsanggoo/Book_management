package com.toyproject.bookmanagement.service;

import org.springframework.stereotype.Service;

import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.entity.User;
import com.toyproject.bookmanagement.exception.CustomException;
import com.toyproject.bookmanagement.exception.ErrorMap;
import com.toyproject.bookmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationSerivce {
	public final UserRepository userRepository;
	
	
	//email 중복 제거
	public void checkduplicatedEmail(String email) throws Exception {
		if (userRepository.findUserByEmail(email) != null) {
			throw new CustomException("duplicated", ErrorMap.builder().put("email", "사용중인 email입니다.").build());
		}
	}
	public void registUser(SignupReqDto signupReqDto) {

	}
 
	
	
}
