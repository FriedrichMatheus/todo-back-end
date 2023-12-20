package com.brickup.todo.dto;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public class ErrorResponseDTO {

	private List<String> error;
	
	public ErrorResponseDTO(List<String> apiErroList) {
		this.error = apiErroList;
	}
	
	public ErrorResponseDTO(String mensagemErro) {
		this.error = Arrays.asList(mensagemErro);
	}

}