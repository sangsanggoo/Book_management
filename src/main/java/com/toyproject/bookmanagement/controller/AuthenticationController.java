package com.toyproject.bookmanagement.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toyproject.bookmanagement.aop.annotation.ValidAspect;
import com.toyproject.bookmanagement.dto.auth.LoginReqDto;
import com.toyproject.bookmanagement.dto.auth.SignupReqDto;
import com.toyproject.bookmanagement.service.AuthenticationSerivce;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth") //모든 요청은 앞에 /auth가 붙음 
@RequiredArgsConstructor
public class AuthenticationController {
	private final AuthenticationSerivce authenticationSerivce;

	//CORS 열어주는거임

	@ValidAspect
	@PostMapping("/register")
	public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto,BindingResult bindingResult)    {
		authenticationSerivce.checkduplicatedEmail(signupReqDto.getEmail());
		authenticationSerivce.signup(signupReqDto);

		return ResponseEntity.ok().body(true);
	}
	@ValidAspect
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginReqDto loginReqDto,BindingResult bindingResult) {
//		System.out.println(authenticationSerivce.signin(loginReqDto));
		return ResponseEntity.ok().body(authenticationSerivce.signin(loginReqDto));
	}
	
	@GetMapping("/authenticated")
	public ResponseEntity<?> authenticated(String accessToken) {
		System.out.println(accessToken);
		return ResponseEntity.ok().body(authenticationSerivce.authenticated(accessToken));
	}
	
}


