package com.cdac.exception_handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cdac.DTO.ApiResponse;
import com.cdac.custom_exceptions.AuthenticationException;
import com.cdac.custom_exceptions.ResourceNotFoundException;

import jakarta.validation.ConstraintViolationException;

/*
 * to declare a spring bean containing common exc handling advice
meant for all
 * rest controllers (all methods)
 */
@RestControllerAdvice //= @ControllerAdvice - cls level 
//+ @ResponseBody - added over methods
public class GlobalExceptionHandler {
	// equivalent to - catch-all block
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
		System.out.println("in catch all");
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiResponse(e.getMessage()));
	}

	// to declare exc handling method - catch block - B.L exception
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException
	(ResourceNotFoundException e) {
		System.out.println("in handle res not found exc");
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponse(e.getMessage()));
	}
	// to declare exc handling method - catch block - P.L exception
	//trigger - in case invalid dto (@Valid)
		@ExceptionHandler(MethodArgumentNotValidException.class)
		public ResponseEntity<?> handleMethodArgumentNotValidException
		(MethodArgumentNotValidException e) {
			System.out.println("in handle method  arg invalid  exc : P.L validation failures");
			//1. Get List of rejected fields
			List<FieldError> rejectedFields = e.getFieldErrors();
			//2. Convert List<FieldError> -> Map<String:Field name,String-def mesg>
			Map<String, String> errorMap = rejectedFields.stream() //Stream<FieldError>
			.collect(
					Collectors.toMap
					(FieldError::getField,FieldError::getDefaultMessage));
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(errorMap);
		}
		
		// to declare exc handling method - catch block - P.L failures for req params or path vars
		@ExceptionHandler(ConstraintViolationException.class)
		public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
			System.out.println("in handle constraint violation  exc");
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(new ApiResponse(e.getMessage()));
		}
		
		// to declare exc handling method - catch block - auth failure
		@ExceptionHandler(AuthenticationException.class)
		public ResponseEntity<?> handleAuthenticationException
		(AuthenticationException e) {
			System.out.println("in handle auth exc");
			return ResponseEntity
					.status(HttpStatus.UNAUTHORIZED)
					.body(new ApiResponse(e.getMessage()));
		}

}
