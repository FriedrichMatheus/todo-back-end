package com.brickup.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.brickup.todo.dto.ErrorResponseDTO;
import com.brickup.todo.exception.NotFoundException;
import com.brickup.todo.exception.WarningException;

@RestControllerAdvice
public class ApplicationAdviceController {

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponseDTO handlingNotFound(Exception ex) {
		String erroStr = ex.getMessage();
		 
		return new ErrorResponseDTO(erroStr);
	}
	@ExceptionHandler(WarningException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponseDTO handlingWarningException(Exception ex) {
		String erroStr = ex.getMessage();
		
		return new ErrorResponseDTO(erroStr);
	}
	
}
