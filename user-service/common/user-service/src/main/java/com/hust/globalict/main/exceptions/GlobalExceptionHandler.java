package com.hust.globalict.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.utils.ResponseObject;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	private final LocalizationUtils localizationUtils;
	
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ResponseObject> handlingDataNotFoundException(Exception exception) {
		return ResponseEntity.badRequest().body(ResponseObject.builder()
				.status(HttpStatus.NOT_FOUND)
				.message(exception.getMessage())
				.build());
	}
	
	@ExceptionHandler(BindException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)  // Nếu validate fail thì trả về 400
	public ResponseEntity<ResponseObject> handleBindException(BindException e) {
	    return ResponseEntity.badRequest().body(ResponseObject.builder()
	    		.status(HttpStatus.BAD_REQUEST)
	    		.message(localizationUtils.getLocalizedMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage()))	
	    		.build());
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)  // Nếu validate fail thì trả về 400
	public ResponseEntity<ResponseObject> handleRuntimeException(RuntimeException exception) {
		return ResponseEntity.badRequest().body(ResponseObject.builder()
				.status(HttpStatus.BAD_REQUEST)
				.message(exception.getMessage())
				.build());
	}
}
