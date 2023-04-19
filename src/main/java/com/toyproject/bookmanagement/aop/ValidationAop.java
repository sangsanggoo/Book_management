package com.toyproject.bookmanagement.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import com.toyproject.bookmanagement.aop.annotation.ValidAspect;
import com.toyproject.bookmanagement.exception.CustomException;

@Aspect
@Component
public class ValidationAop {
	@Pointcut("@annotation(com.toyproject.bookmanagement.aop.annotation.ValidAspect)")
	private void pointCut() {}
	@Around("pointCut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		
		Object[] args = joinPoint.getArgs();
		BindingResult bindingResult = null;
		for(Object arg : args) {
			if(arg.getClass() == BeanPropertyBindingResult.class) {
				bindingResult = (BindingResult) arg;
			}
		}
		if (bindingResult.hasErrors()) {
			Map<String, String> errorData = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error -> {
				errorData.put(error.getField(),error.getDefaultMessage());
			});
			
			throw new CustomException("validation Failed",errorData);
		}
		
		return joinPoint.proceed();
	}
 }
	
